package com.example.taskplanner.data

import android.util.Log
import com.example.taskplanner.data.model.Day
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeNotes
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class Repository @Inject constructor(private val notesDao: NotesDao) {

    suspend fun getPageDays(firstCount: Int): List<Day> {
        var changeableCount = firstCount
        Log.d("@@@", "changeableCount =$changeableCount")
        return List(10) {
            val date = LocalDate.now().plusDays(changeableCount++.toLong())
            val list = notesDao.getAllNotesByDate(date)
            Day(date, list)
        }
    }

    suspend fun deleteNote(note: TypeNotes) {
        when(note) {
            is Medications -> notesDao.deleteMedications(note)
            is Note -> notesDao.deleteNote(note)
            is Products -> notesDao.deleteProducts(note)
            is Reminder -> notesDao.deleteReminder(note)
        }
    }

    suspend fun addNote(note: Note) {
        notesDao.insertNote(note)
    }

    suspend fun addReminder(reminder: Reminder) {
        notesDao.insertReminder(reminder)
    }
}
