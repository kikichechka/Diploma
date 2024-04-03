package com.example.taskplanner.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskplanner.domain.Day
import com.example.taskplanner.databinding.FragmentPlannerItemScrollDayBinding
import com.example.taskplanner.view.OnItemClickDay

class CalendarAdapter(private val listDay: List<Day>, private val callback: OnItemClickDay) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = FragmentPlannerItemScrollDayBinding.inflate(LayoutInflater.from(parent.context))
        return CalendarViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listDay.size
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(listDay[position])
    }

    inner class CalendarViewHolder(private val binding: FragmentPlannerItemScrollDayBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(day: Day) {
            binding.date.text = day.toString()
            if (day.list.isNotEmpty()) {
                binding.notTask.text = ""
                binding.scrollItemTask.adapter = NoteListAdapter(day.list)
            }
            binding.root.setOnClickListener {
                callback.onDayClick(day)
            }
        }
    }

}


