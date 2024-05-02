package com.example.taskplanner.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.taskplanner.data.model.entity.Day

class DiffUtilCallbackDay : DiffUtil.ItemCallback<Day>() {
    override fun areItemsTheSame(oldItem: Day, newItem: Day): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: Day, newItem: Day): Boolean {
        return oldItem == newItem
    }
}
