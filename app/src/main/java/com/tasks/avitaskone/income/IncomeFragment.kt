package com.tasks.avitaskone.income

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tasks.avitaskone.AccountViewModel
import com.tasks.avitaskone.core.BaseFragment
import com.tasks.avitaskone.HeaderAdapter
import com.tasks.avitaskone.databinding.FragmentIncomeLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IncomeFragment : BaseFragment<FragmentIncomeLayoutBinding>() {

	private val viewModel: AccountViewModel by activityViewModels()

	private val incomeAdapter by lazy {
		IncomeAdapter {
			Snackbar.make(binding.textView2, it, Snackbar.LENGTH_LONG).show()
		}
	}
	private val headerAdapter by lazy { HeaderAdapter() }
	private val concatAdapter by lazy { ConcatAdapter(headerAdapter, incomeAdapter) }

	override fun inflateLayout() = FragmentIncomeLayoutBinding.inflate(layoutInflater)

	override fun runOnViewCreated() {
		setupRecyclerView()

		lifecycleScope.launch {
			viewModel.incomeStateFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
				binding.loadingBar.apply {
					visibility = if (it.isLoading) View.VISIBLE else View.GONE
				}

				if (it.incomes.isNotEmpty()) {
					incomeAdapter.submitList(it.incomes)
				}
			}
		}
	}

	private fun setupRecyclerView() {
		binding.incomeRecycler.apply {
			layoutManager = LinearLayoutManager(requireContext())
			adapter = concatAdapter
			hasFixedSize()
		}
	}
}