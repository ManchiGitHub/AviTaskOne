package com.tasks.avitaskone

import com.tasks.avitaskone.yearly.YearlyItem

data class AccountState(
	val yearlyList: List<YearlyItem> = emptyList(),
	val isAccountAtRisk: Boolean,
	val balance: Double
)