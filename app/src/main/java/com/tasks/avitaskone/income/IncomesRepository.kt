package com.tasks.avitaskone.income

import com.tasks.avitaskone.expense.ExpenseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class IncomesRepository @Inject constructor() {

	fun incomesFlow() = flow {
		delay(1000)
		emit(fetchIncomes())
	}.flowOn(Dispatchers.IO)

	companion object {
		private val fakeData = arrayListOf(
			IncomeItem(1, "28/23", "Bit", "50"),
			IncomeItem(2, "2/23", "Salary", "50"),
			IncomeItem(3, "23/23", "Successful bank robbery", "6000"),
			IncomeItem(4, "12/23", "Donation", "200"),
			IncomeItem(5, "04/23", "Salary", "200"),
			IncomeItem(6, "08/23", "Sold some stocks", "10"),
			IncomeItem(7, "12/23", "Failed bank robbery", "500"),
			IncomeItem(8, "13/23", "Donation", "69.5"),
			IncomeItem(9, "28/23", "Found some money on the floor", "50"),
			IncomeItem(10, "01/23", "Salary", "50"),
			IncomeItem(11, "05/23", "Bit", "60"),
			IncomeItem(12, "10/23", "Salary", "250"),
			IncomeItem(13, "08/23", "Bit", "20"),
			IncomeItem(14, "28/23", "Donation", "10"),
			IncomeItem(15, "26/23", "Lottery Win", "5000"),
			IncomeItem(16, "16/23", "Salary", "69")
		)
	}

	private fun fetchIncomes(): List<IncomeItem> {
		return ArrayList(fakeData)
	}

	fun add(date: String, title: String, amount: Double) {
		val lastID = fakeData.last().id
		fakeData.add(IncomeItem(lastID + 1, date, title, amount.toString()))
	}
}