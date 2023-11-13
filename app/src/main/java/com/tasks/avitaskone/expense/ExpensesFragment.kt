package com.tasks.avitaskone.expense

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tasks.avitaskone.AccountViewModel
import com.tasks.avitaskone.core.BaseFragment
import com.tasks.avitaskone.HeaderAdapter
import com.tasks.avitaskone.databinding.FragmentExpensesLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExpensesFragment : BaseFragment<FragmentExpensesLayoutBinding>() {

	private val viewModel: AccountViewModel by activityViewModels()

	private val expensesAdapter by lazy {
		ExpensesAdapter {
			Snackbar.make(binding.textView, it, Snackbar.LENGTH_LONG).show()
		}
	}
	private val headerAdapter by lazy { HeaderAdapter() }
	private val concatAdapter by lazy { ConcatAdapter(headerAdapter, expensesAdapter) }

	override fun inflateLayout() = FragmentExpensesLayoutBinding.inflate(layoutInflater)

	override fun runOnViewCreated() {
		setupRecyclerView()

		lifecycleScope.launch {
			viewModel.expensesStateFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
				binding.loadingBar.apply {
					visibility = if (it.isLoading) View.VISIBLE else View.GONE
				}
				if (it.expenses.isNotEmpty()) {
					expensesAdapter.submitList(it.expenses)
				}
			}
		}
	}

	private fun setupRecyclerView() {
		binding.expensesRecycler.apply {
			layoutManager = LinearLayoutManager(requireContext())
			adapter = concatAdapter
			hasFixedSize()
		}
	}
}