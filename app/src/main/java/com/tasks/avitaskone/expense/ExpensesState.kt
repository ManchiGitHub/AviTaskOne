package com.tasks.avitaskone.expense

data class ExpensesState(
	val expenses: List<ExpenseItem> = emptyList(),
	val isLoading: Boolean,
)