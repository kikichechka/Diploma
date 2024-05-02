package com.example.taskplanner.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskplanner.R
import com.example.taskplanner.data.model.entity.Day
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.TypeTask
import com.example.taskplanner.databinding.FragmentPlannerItemScrollDayBinding
import kotlinx.parcelize.IgnoredOnParcel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarAdapter(
    private val onClickTaskDelete: (TypeTask) -> Unit,
    private val onClickTaskChange: (TypeTask) -> Unit,
    private val onClickTaskChangeFinished: (TypeTask) -> Unit,
    private val onClickProductChangeFinished: (Product) -> Unit
) : PagingDataAdapter<Day, CalendarViewHolder>(DiffUtilCallbackDay()) {

    @IgnoredOnParcel
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) {

            with(holder.binding) {
                date.text = item.date.format(formatter)
                if (item.date == LocalDate.now()) {
                    thisDate.text = holder.binding.root.context.getString(R.string.today)
                } else {
                    thisDate.text = ""
                }
                if (item.list.isNotEmpty()) {
                    notTask.isVisible = false
                    scrollItemTask.adapter = AllNotesListAdapter(
                        onDeleteTask = { note -> onClickTaskDelete(note) },
                        onClickChangeFinishedProduct = { product ->
                            onClickProductChangeFinished(
                                product
                            )
                        },
                        onClickChangeFinishedTask = { note -> onClickTaskChangeFinished(note) },
                        onChangeTask = { note -> onClickTaskChange(note) },
                        day = item
                    )
                } else {
                    notTask.isVisible = true
                    notTask.text = root.context.getString(R.string.not_task)
                    scrollItemTask.adapter = null
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
