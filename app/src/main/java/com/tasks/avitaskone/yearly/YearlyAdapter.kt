package com.tasks.avitaskone.yearly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tasks.avitaskone.databinding.LayoutYearlyItemBinding

class YearlyAdapter : RecyclerView.Adapter<YearlyAdapter.YearlyViewHolder>() {

	private val differCallback = object : DiffUtil.ItemCallback<YearlyItem>() {
		override fun areItemsTheSame(oldItem: YearlyItem, newItem: YearlyItem): Boolean {
			// Implement your logic to check if items are the same
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(oldItem: YearlyItem, newItem: YearlyItem): Boolean {
			// Implement your logic to check if the content of items is the same
			return oldItem == newItem
		}
	}

	private val differ = AsyncListDiffer(this, differCallback)

	class YearlyViewHolder(private val binding: LayoutYearlyItemBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(item: YearlyItem) {
			binding.tvDate.text = item.date
			binding.tvIncome.text = item.income.toString()
			binding.tvExpense.text = item.expense.toString()
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearlyViewHolder {
		val binding =
			LayoutYearlyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return YearlyViewHolder(binding)
	}

	override fun onBindViewHolder(holder: YearlyViewHolder, position: Int) {
		holder.bind(differ.currentList[position])
	}

	override fun getItemCount() = differ.currentList.size

	fun submitList(list: List<YearlyItem>){
		differ.submitList(list)
	}
}