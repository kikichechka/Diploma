package com.example.taskplanner.data.model.repository

import android.util.Log
import com.example.taskplanner.data.NotesDao
import com.example.taskplanner.data.model.Day
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeNotes
import java.time.LocalDate
import javax.inject.Inject

class ListNoteRepository @Inject constructor(private val notesDao: NotesDao) {

    suspend fun getPageDays(firstCount: Int): List<Day> {
        var changeableCount = firstCount
        Log.d("@@@", "changeableCount =$changeableCount")
        return List(50) {
            val date = LocalDate.now().minusDays(25).plusDays(changeableCount++.toLong())
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

    suspend fun changeFinishNote(note: TypeNotes) {
        when (note) {
            is Medications -> {
                note.finished = !note.finished
                notesDao.updateMedications(note)
            }
            is Note -> {
                note.finished = !note.finished
                notesDao.updateNote(note)
            }
            is Products -> {
                note.finished = !note.finished
                notesDao.updateProducts(note)
            }
            is Reminder -> {
                note.finished = !note.finished
                notesDao.updateReminder(note)
            }
        }
    }
}
