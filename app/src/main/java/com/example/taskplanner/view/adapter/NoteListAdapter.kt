package com.example.taskplanner.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.taskplanner.domain.TypeNotes
import com.example.taskplanner.databinding.ItemTaskBinding
import com.example.taskplanner.databinding.NotificationItemBinding

class NoteListAdapter(private val listNote: List<TypeNotes>): RecyclerView.Adapter<NoteListAdapter.ListNoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNoteViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context))
        return ListNoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listNote.size
    }

    override fun onBindViewHolder(holder: ListNoteViewHolder, position: Int) {
        holder.onBind(listNote[position])
    }

    class ListNoteViewHolder(private val binding:ItemTaskBinding):RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(note: TypeNotes) {
            if (!note.finished) {
                binding.titleTask.text = note.title
            } else {
                binding.titleTask.text = HtmlCompat.fromHtml(("<strike>" + note.title + "</strike>"), HtmlCompat.FROM_HTML_MODE_LEGACY)
            }

            if (note is TypeNotes.Reminder) {
                val bindingNotification: NotificationItemBinding = NotificationItemBinding.inflate(LayoutInflater.from(binding.root.context))
                bindingNotification.time.text = String.format("%02d:%02d", note.time.hour, note.time.minute)
                binding.containerForNotifications.addView(bindingNotification.root)
            }
        }
    }
}