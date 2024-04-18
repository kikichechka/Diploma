package com.example.taskplanner.data.model.repository

import com.example.taskplanner.data.NotesDao
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.Reminder
import javax.inject.Inject

class AddNoteRepository @Inject constructor(private val notesDao: NotesDao) {

    suspend fun addNote(note: Note) {
        notesDao.insertNote(note)
    }

    suspend fun addReminder(reminder: Reminder) {
        notesDao.insertReminder(reminder)
    }

    suspend fun addProducts(products: Products) {
        notesDao.insertProducts(products)
    }

    suspend fun addMedications(medications: Medications) {
        notesDao.insertMedications(medications)
    }
}
