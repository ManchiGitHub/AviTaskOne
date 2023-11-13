package com.tasks.avitaskone.expense

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tasks.avitaskone.databinding.LayoutMovementItemBinding

class ExpensesAdapter(
	private val onClick: (description: String) -> Unit
) :
	RecyclerView.Adapter<ExpensesAdapter.ExpenseViewHolder>() {

	private val differCallback = object : DiffUtil.ItemCallback<ExpenseItem>() {
		override fun areItemsTheSame(oldItem: ExpenseItem, newItem: ExpenseItem): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(oldItem: ExpenseItem, newItem: ExpenseItem): Boolean {
			return oldItem == newItem
		}
	}

	private val differ = AsyncListDiffer(this, differCallback)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
		val binding =
			LayoutMovementItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ExpenseViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
		holder.bind(differ.currentList[position])
	}

	override fun getItemCount() = differ.currentList.size

	inner class ExpenseViewHolder(private val binding: LayoutMovementItemBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(item: ExpenseItem) {
			binding.tvDate.text = item.date
			binding.tvTitle.text = item.title
			binding.tvAmount.text = item.amount
		}

		init {
			itemView.setOnClickListener {
				onClick(binding.tvTitle.text.toString())
			}
		}
	}

	fun submitList(list: List<ExpenseItem>) {
		differ.submitList(list)
	}
}

