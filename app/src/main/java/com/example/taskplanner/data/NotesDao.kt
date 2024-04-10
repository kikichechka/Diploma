package com.example.taskplanner.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeNotes
import java.time.LocalDate

@Dao
interface NotesDao {
    @Query("SELECT * FROM note WHERE date = :date")
    suspend fun getAllNotes(date: LocalDate): List<Note>

    @Query("SELECT * FROM reminder WHERE date = :date")
    suspend fun getAllReminders(date: LocalDate): List<Reminder>

    @Query("SELECT * FROM products WHERE date = :date")
    suspend fun getAllListProducts(date: LocalDate): List<Products>

    @Query("SELECT * FROM medications WHERE date = :date")
    suspend fun getAllListMedications(date: LocalDate): List<Medications>

    suspend fun getAllNotesByDate(date: LocalDate) : MutableList<TypeNotes> {
        val list = mutableListOf<TypeNotes>().apply {
            addAll(getAllNotes(date) + getAllReminders(date) + getAllListProducts(date) + getAllListMedications(date))
        }
        return list
    }

    @Insert(entity = Note::class)
    suspend fun insertNote(note: Note)

    @Insert(entity = Reminder::class)
    suspend fun insertReminder(reminder: Reminder)

    @Insert(entity = Products::class)
    suspend fun insertProducts(products: Products)

    @Insert(entity = Medications::class)
    suspend fun insertMedications(medications: Medications)

    @Delete(entity = Note::class)
    suspend fun deleteNote(note: Note)

    @Delete(entity = Reminder::class)
    suspend fun deleteReminder(reminder: Reminder)

    @Delete(entity = Products::class)
    suspend fun deleteProducts(products: Products)

    @Delete(entity = Medications::class)
    suspend fun deleteMedications(medications: Medications)
}
