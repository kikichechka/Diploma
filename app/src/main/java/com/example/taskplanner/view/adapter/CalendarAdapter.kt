package com.example.taskplanner.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.taskplanner.R
import com.example.taskplanner.data.model.Day
import com.example.taskplanner.data.model.entity.TypeNotes
import com.example.taskplanner.databinding.FragmentPlannerItemScrollDayBinding
import kotlinx.parcelize.IgnoredOnParcel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarAdapter(
    private val onClickNoteDelete: (TypeNotes) -> Unit
) : PagingDataAdapter<Day, CalendarViewHolder>(DiffUtilCallback()) {

    @IgnoredOnParcel
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            createDay(item, holder.binding)
        }
    }

    private fun createDay(item: Day, binding: FragmentPlannerItemScrollDayBinding) {
        binding.date.text = item.date.format(formatter)
        if (item.list.isNotEmpty()) {
            binding.notTask.text = ""
            createNoteListAdapter(item, binding, item)
        } else {
            binding.notTask.text = binding.root.context.getString(R.string.not_task)
            binding.scrollItemTask.adapter = null
        }
    }

    private fun createNoteListAdapter(item: Day, binding: FragmentPlannerItemScrollDayBinding, day: Day) {
        binding.scrollItemTask.adapter = NoteListAdapter(
            onNoteClick = { note ->
                onClickDeleteNote(note)
                item.list.remove(note)
                createDay(item, binding)
            },
            day = item
        )
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

    private fun onClickDeleteNote(note: TypeNotes) {
        onClickNoteDelete(note)
    }
}
