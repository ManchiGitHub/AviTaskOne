package com.tasks.avitaskone

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.tasks.avitaskone.core.BaseFragment
import com.tasks.avitaskone.databinding.FragmentAccountLayoutBinding
import com.tasks.avitaskone.yearly.YearlyAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountLayoutBinding>() {

	private val accountViewModel: AccountViewModel by activityViewModels()

	private val yearlyAdapter by lazy { YearlyAdapter() }

	override fun inflateLayout() = FragmentAccountLayoutBinding.inflate(layoutInflater)

	override fun runOnViewCreated() {
		setupPageAdapterWithTabs()
		setupOverviewRecycler()
		observeAccountState()
		initViews()
	}

	private fun showAddMovementDialogFragment(command: UserAction) {
		val dialogFragment = AddMovementDialogFragment.newInstance(command)
		dialogFragment.show(parentFragmentManager, "AddMovementDialog")
		dialogFragment.setCallback { date, title, amount, isExpense ->
			accountViewModel.onAddMovement(date,title,amount,isExpense)
		}
	}

	private fun observeAccountState() {
		lifecycleScope.launch {
			accountViewModel.accountStateFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
				.collect {
					handleYearlyData(it)
					handleBalance(it)
					handleAccountRisk(it)
				}
		}
	}

	private fun initViews() {
		binding.addMovementFab.setOnClickListener {
			when (binding.accountTabLayout.selectedTabPosition) {
				0 -> showAddMovementDialogFragment(UserAction.AddExpenseDialog) // Expense tab
				1 -> showAddMovementDialogFragment(UserAction.AddIncomeDialog) // Income tab
			}
		}
	}

	private fun handleYearlyData(accountState: AccountState) {
		yearlyAdapter.submitList(accountState.yearlyList)
	}

	private fun handleAccountRisk(accountState: AccountState) {
		if (accountState.isAccountAtRisk) {
			showAccountRiskSnackbar(binding.accountTabLayout)
		}
	}

	private fun handleBalance(accountState: AccountState) {

		if (accountState.balance == 0.0) {
			return
		}

		if (accountState.balance > 0) {
			binding.balanceTv.setTextColor(
				ContextCompat.getColor(
					requireContext(),
					R.color.green
				)
			)
		} else {
			binding.balanceTv.setTextColor(
				ContextCompat.getColor(
					requireContext(),
					R.color.red
				)
			)
		}

		binding.balanceTv.visibility = View.VISIBLE
		val formatter = DecimalFormat("#,###.00")
		formatter.isDecimalSeparatorAlwaysShown = true
		formatter.negativePrefix = "-"

		// Format the balance
		val formattedBalance = formatter.format(accountState.balance)
		val balanceText = getString(R.string.balance, formattedBalance)
		binding.balanceTv.text = balanceText
	}

	private fun showAccountRiskSnackbar(view: View) {
		val snackbar = Snackbar.make(view, "Your Account is at risk!", Snackbar.LENGTH_INDEFINITE)
		snackbar.setAction("Learn more") {}
		snackbar.anchorView = view // Anchors above RecyclerView
		snackbar.show()
	}

	private fun setupOverviewRecycler() {
		binding.yearlyRecycler.apply {
			layoutManager =
				LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
			adapter = yearlyAdapter
			hasFixedSize()
		}
	}

	private fun setupPageAdapterWithTabs() {
		binding.accountViewPager.adapter = ViewPagerAdapter(requireActivity())
		TabLayoutMediator(binding.accountTabLayout, binding.accountViewPager) { tab, position ->
			tab.text = if (position == 0) "Expenses" else "Income"
		}.attach()
	}
}

