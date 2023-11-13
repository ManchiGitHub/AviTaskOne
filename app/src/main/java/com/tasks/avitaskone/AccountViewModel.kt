package com.tasks.avitaskone

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tasks.avitaskone.expense.ExpensesRepository
import com.tasks.avitaskone.expense.ExpensesState
import com.tasks.avitaskone.income.IncomeState
import com.tasks.avitaskone.income.IncomesRepository
import com.tasks.avitaskone.yearly.YearlyItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
	application: Application,
	private val expensesRepository: ExpensesRepository,
	private val incomesRepository: IncomesRepository
) : AndroidViewModel(application) {

	private val _accountStateFlow =
		MutableStateFlow(AccountState(isAccountAtRisk = false, balance = 0.0))
	val accountStateFlow = _accountStateFlow.asStateFlow()

	private val _expensesState = MutableStateFlow(ExpensesState(isLoading = false))
	val expensesStateFlow = _expensesState.asStateFlow()

	private val _incomeState = MutableStateFlow(IncomeState(isLoading = false))
	val incomeStateFlow = _incomeState.asStateFlow()

	init {
		collectExpenses()
		collectIncomes()
		getYearlyOverview()
	}

	private fun getYearlyOverview() {
		viewModelScope.launch {
			combine(
				incomesRepository.incomesFlow(),
				expensesRepository.expensesFlow()
			) { incomes, expenses ->

				val combinedMap = mutableMapOf<String, Pair<Double, Double>>()

				var totalIncome = 0.0
				var totalExpense = 0.0

				// Handle incomes
				incomes.forEach { income ->
					val existing = combinedMap[income.date] ?: Pair(0.0, 0.0)
					combinedMap[income.date] =
						existing.copy(first = existing.first + income.amount.toDouble())
					totalIncome += income.amount.toDouble()
				}

				// Handle expenses
				expenses.forEach { expense ->
					val existing = combinedMap[expense.date] ?: Pair(0.0, 0.0)
					combinedMap[expense.date] =
						existing.copy(second = existing.second + expense.amount.toDouble())
					totalExpense += expense.amount.toDouble()
				}

				val combinedList = combinedMap.map { (date, totals) ->
					YearlyItem(
						0,
						date,
						totals.first,
						totals.second
					)
				}.sortedByDescending { it.date }


				val isAtRisk = totalExpense > totalIncome
				AccountState(
					combinedList,
					isAccountAtRisk = isAtRisk,
					balance = (totalIncome - totalExpense)
				)

			}.flowOn(Dispatchers.IO)
				.collect { accountState ->
					_accountStateFlow.update {
						it.copy(
							yearlyList = accountState.yearlyList,
							isAccountAtRisk = accountState.isAccountAtRisk,
							balance = accountState.balance
						)
					}
				}
		}
	}

	fun onAddMovement(date: String, title: String, amount: Double, isExpense: Boolean) {
		if (isExpense) {
			expensesRepository.add(date, title, amount)
			collectExpenses()
		} else {
			collectIncomes()
			incomesRepository.add(date, title, amount)
		}

		getYearlyOverview()
	}

	private fun collectIncomes() {
		viewModelScope.launch {
			incomesRepository.incomesFlow().collect { newList ->
				_incomeState.update { state ->
					state.copy(incomes = newList, isLoading = false)
				}
			}
		}
	}

	private fun collectExpenses() {
		viewModelScope.launch {
			expensesRepository.expensesFlow().collect { newList ->
				_expensesState.update { state ->
					state.copy(expenses = newList, isLoading = false)
				}
			}
		}
	}
}

