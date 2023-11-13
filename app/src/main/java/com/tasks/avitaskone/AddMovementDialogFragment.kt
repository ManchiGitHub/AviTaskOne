package com.tasks.avitaskone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.tasks.avitaskone.databinding.FragmentAddMovementDialogLayoutBinding

class AddMovementDialogFragment : DialogFragment() {

	private lateinit var userAction: UserAction

	private var callback: ((date: String, title: String, amount: Double, isExpense: Boolean) -> Unit)? =
		null

	companion object {
		private const val ARG_TITLE = "ARG_TITLE"

		fun newInstance(intent: UserAction): AddMovementDialogFragment {
			val args = Bundle().apply {
				when (intent) {
					UserAction.AddExpenseDialog -> {
						putInt(ARG_TITLE, intent.ordinal)
					}
					UserAction.AddIncomeDialog -> {
						putInt(ARG_TITLE, intent.ordinal)
					}
				}
			}

			return AddMovementDialogFragment().apply {
				arguments = args
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setStyle(STYLE_NO_TITLE, R.style.RoundShapeDialog)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding = FragmentAddMovementDialogLayoutBinding.inflate(inflater, container, false)
		setTitle(binding)

		initButtons(binding)
		return binding.root
	}

	private fun setTitle(binding: FragmentAddMovementDialogLayoutBinding) {
		userAction = UserAction.fromInt(arguments?.getInt(ARG_TITLE) ?: 0)
		binding.dialogTitle.text = "New $userAction"
	}

	private fun initButtons(binding: FragmentAddMovementDialogLayoutBinding) {
		binding.okButton.setOnClickListener {
			val date = binding.dateInput.text.toString()
			val regexDate = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])\$".toRegex()

			if (!date.matches(regexDate)) {
				Toast.makeText(requireContext(), "Bad date format", Toast.LENGTH_SHORT).show()
			}

			val title = binding.titleInput.text.toString()
			val amount = binding.amountInput.text.toString().toDouble()

			if (title.isNotEmpty() && amount > 0 && date.matches(regexDate)) {
				val isExpense = (userAction == UserAction.AddExpenseDialog)
				callback?.let { callback -> callback(date, title, amount, isExpense) }
				dismiss()
			}

		}

		binding.cancelButton.setOnClickListener {
			dismiss()
		}
	}

	override fun onStart() {
		super.onStart()
		val width = (resources.displayMetrics.widthPixels * 0.85).toInt() // 85% of screen width
		val height = (resources.displayMetrics.heightPixels * 0.75).toInt() // 85% of screen width
		dialog?.window?.setLayout(width, height)
	}

	fun setCallback(callback: (date: String, title: String, amount: Double, isExpense: Boolean) -> Unit) {
		this.callback = callback
	}
}

