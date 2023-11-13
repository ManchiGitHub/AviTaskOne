package com.tasks.avitaskone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tasks.avitaskone.databinding.LayoutExpenseHeaderBinding

class HeaderAdapter : RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
		val binding =
			LayoutExpenseHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return HeaderViewHolder(binding)
	}

	override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
		// Binding logic, if any
	}

	override fun getItemCount() = 1

	class HeaderViewHolder(binding: LayoutExpenseHeaderBinding) : RecyclerView.ViewHolder(binding.root)
}