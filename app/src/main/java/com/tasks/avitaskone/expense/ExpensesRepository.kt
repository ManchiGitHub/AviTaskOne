package com.tasks.avitaskone.expense

import com.tasks.avitaskone.income.IncomeItem
import com.tasks.avitaskone.income.IncomesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class ExpensesRepository @Inject constructor() {

	fun expensesFlow() = flow {
			delay(2000)
			emit(fetchExpenses())
	}.take(1).flowOn(Dispatchers.IO)

	companion object {
		private val fakeData = arrayListOf(
			ExpenseItem(1, "28/23", "Groceries", "50"),
			ExpenseItem(2, "2/23", "Games", "50"),
			ExpenseItem(3, "23/23", "Movies", "60"),
			ExpenseItem(4, "12/23", "Groceries", "200"),
			ExpenseItem(5, "04/23", "Take Out", "20000"),
			ExpenseItem(6, "08/23", "Water Bill", "1000"),
			ExpenseItem(7, "12/23", "Electricity Bill", "50"),
			ExpenseItem(8, "13/23", "Rent", "6900"),
			ExpenseItem(9, "28/23", "Groceries", "506"),
			ExpenseItem(10, "01/23", "Games", "50"),
			ExpenseItem(11, "05/23", "Movies", "605"),
			ExpenseItem(12, "10/23", "Groceries", "200"),
			ExpenseItem(13, "08/23", "Take Out", "20"),
			ExpenseItem(14, "28/23", "Water Bill", "100"),
			ExpenseItem(15, "26/23", "Electricity Bill", "50"),
			ExpenseItem(16, "16/23", "Rent", "69")
		)
	}

	private fun fetchExpenses(): List<ExpenseItem> {
		return ArrayList(fakeData)
	}

	fun add(date: String, title: String, amount: Double) {
		val lastID = fakeData.last().id
		fakeData.add(ExpenseItem(lastID + 1, date, title, amount.toString()))
	}
}