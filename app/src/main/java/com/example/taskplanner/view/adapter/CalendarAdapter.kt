package com.example.taskplanner.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskplanner.domain.Day
import com.example.taskplanner.databinding.FragmentPlannerItemScrollDayBinding
import com.example.taskplanner.view.OnItemClickDay

class CalendarAdapter(private val callback: OnItemClickDay) :
    PagingDataAdapter<Day, CalendarViewHolder>(DiffUtilCallback())
{
    private val listDay: List<Day> = listOf()
    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            date.text = item.toString()
            if (item?.list != null) {
                notTask.text = ""
                scrollItemTask.adapter = NoteListAdapter(item.list)
            }
            root.setOnClickListener {
                callback.onDayClick(item!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        return CalendarViewHolder(
            FragmentPlannerItemScrollDayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
//        val binding = FragmentPlannerItemScrollDayBinding.inflate(LayoutInflater.from(parent.context))
//        return CalendarViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int {
//        return listDay.size
//    }
//
//    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
//        holder.bind(listDay[position])
//    }

//    inner class CalendarViewHolder(private val binding: FragmentPlannerItemScrollDayBinding) : RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(day: Day) {
//            binding.date.text = day.toString()
//            if (day.list.isNotEmpty()) {
//                binding.notTask.text = ""
//                binding.scrollItemTask.adapter = NoteListAdapter(day.list)
//            }
//            binding.root.setOnClickListener {
//                callback.onDayClick(day)
//            }
//        }
//    }

}

class CalendarViewHolder(val binding: FragmentPlannerItemScrollDayBinding) : RecyclerView.ViewHolder(binding.root)

class DiffUtilCallback : DiffUtil.ItemCallback<Day> () {
    override fun areItemsTheSame(oldItem: Day, newItem: Day): Boolean {
        return oldItem.date == newItem.date
    }
    override fun areContentsTheSame(oldItem: Day, newItem: Day): Boolean {
        return oldItem == newItem
    }
}


