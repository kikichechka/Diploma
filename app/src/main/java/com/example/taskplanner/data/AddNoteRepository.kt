package com.example.taskplanner.data

import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Reminder
import javax.inject.Inject

class AddNoteRepository @Inject constructor(private val notesDao: NotesDao) {

    suspend fun addNote(note: Note) {
        notesDao.insertNote(note)
    }

    suspend fun addReminder(reminder: Reminder) {
        notesDao.insertReminder(reminder)
    }
}
