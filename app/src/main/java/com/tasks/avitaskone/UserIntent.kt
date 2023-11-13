package com.tasks.avitaskone

enum class UserAction {
	AddExpenseDialog {
		override fun toString(): String {
			return "Expense"
		}
	},
	AddIncomeDialog {
		override fun toString(): String {
			return "Income"
		}
	};

	companion object {
		fun fromInt(value: Int) = values().getOrElse(value) { AddExpenseDialog }
	}
}

class Event<out T>(private val content: T) {
	var hasBeenHandled = false
		private set

	fun getContentIfNotHandled(): T? {
		return if (hasBeenHandled) {
			null
		} else {
			hasBeenHandled = true
			content
		}
	}
}