package com.example.taskplanner.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskplanner.R
import com.example.taskplanner.data.model.Day
import com.example.taskplanner.data.model.entity.TypeNotes
import com.example.taskplanner.databinding.FragmentPlannerItemScrollDayBinding
import kotlinx.parcelize.IgnoredOnParcel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarAdapter(
    private val onClickNoteDelete: (TypeNotes) -> Unit,
    private val onClickNoteChange: (TypeNotes) -> Unit,
    private val onClickNoteChangeFinished: (TypeNotes) -> Unit,
    private val onClickProductChangeFinished: (TypeNotes, Int) -> Unit
) : PagingDataAdapter<Day, CalendarViewHolder>(DiffUtilCallback()) {

    @IgnoredOnParcel
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) {

            with(holder.binding) {
                date.text = item.date.format(formatter)

                if (item.list.isNotEmpty()) {
                    notTask.isVisible = false
                    if (item.date == LocalDate.now()) {
                        thisDate.text = holder.binding.root.context.getString(R.string.today)
                    } else {
                        thisDate.text = ""
                    }
                    scrollItemTask.adapter = AllNotesListAdapter(
                        onDeleteNote = { note -> onClickNoteDelete(note) },
                        onClickChangeFinishedProduct = { prod, positionProduct ->
                            onClickProductChangeFinished(
                                prod,
                                positionProduct
                            )
                        },
                        onClickChangeFinishedNote = { note -> onClickNoteChangeFinished(note) },
                        onChangeNote = { note -> onClickNoteChange(note) },
                        day = item
                    )
                } else {
                    notTask.isVisible = true
                    notTask.text = root.context.getString(R.string.not_task)
                    scrollItemTask.adapter = null
                    thisDate.text = ""

                }
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
}

class CalendarViewHolder(val binding: FragmentPlannerItemScrollDayBinding) :
    RecyclerView.ViewHolder(binding.root)
