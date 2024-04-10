package com.example.taskplanner.view.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.taskplanner.data.model.Day
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeNotes
import com.example.taskplanner.databinding.ItemTaskBinding
import java.time.LocalDate

class NoteListAdapter(
    private val onNoteClick: (TypeNotes) -> Unit,
    private val day: Day
) :
    RecyclerView.Adapter<NoteListAdapter.ListNoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNoteViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context))
        return ListNoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return day.list.size
    }

    override fun onBindViewHolder(holder: ListNoteViewHolder, position: Int) {
        val itemNote = day.list[position]

        with(holder.binding) {
            if (!itemNote.finished) {
                titleTask.text = itemNote.title
            } else {
                titleTask.text = HtmlCompat.fromHtml(
                    ("<strike>" + itemNote.title + "</strike>"),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }

            buttonSetting.setOnClickListener {
//                holder.binding.root.
                onNoteClick(itemNote)
                Log.d("@@@" , "holder.bindingAdapterPosition ${holder.bindingAdapterPosition}")
//                holder.bindingAdapterPosition
//                day.list.removeAt(position)
            }

            when (itemNote) {
                is Reminder -> {
                    containerForNotifications.isVisible = true
                    time.text = String.format("%02d:%02d", itemNote.time.hour, itemNote.time.minute)
                    return
                }

                is Medications -> {}
                is Note -> {}
                is Products -> {
                }
            }
        }
    }

    class ListNoteViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root)
}
