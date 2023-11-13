package com.tasks.avitaskone.income

data class IncomeState(
	val incomes: List<IncomeItem> = emptyList(),
	val isLoading: Boolean,
)