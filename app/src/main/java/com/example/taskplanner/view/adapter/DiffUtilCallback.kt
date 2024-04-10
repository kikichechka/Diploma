package com.example.taskplanner.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.taskplanner.data.model.Day

class DiffUtilCallback : DiffUtil.ItemCallback<Day>() {
    override fun areItemsTheSame(oldItem: Day, newItem: Day): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: Day, newItem: Day): Boolean {
        return oldItem == newItem
    }
}
