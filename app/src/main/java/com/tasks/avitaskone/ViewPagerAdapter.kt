package com.tasks.avitaskone

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tasks.avitaskone.expense.ExpensesFragment
import com.tasks.avitaskone.income.IncomeFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
	override fun createFragment(position: Int): Fragment {
		return if (position == 0) ExpensesFragment() else IncomeFragment()
	}

	override fun getItemCount(): Int {
		return 2 // Two tabs
	}
}