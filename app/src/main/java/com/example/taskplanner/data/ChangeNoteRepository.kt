package com.example.taskplanner.data

import android.util.Log
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeNotes
import javax.inject.Inject

class ChangeNoteRepository @Inject constructor(private val notesDao: NotesDao) {

    suspend fun changeNote(note: TypeNotes) {
        when(note) {
            is Medications -> notesDao.updateMedications(note)
            is Note -> notesDao.updateNote(note)
            is Products -> notesDao.updateProducts(note)
            is Reminder -> notesDao.updateReminder(note)
        }
    }
}