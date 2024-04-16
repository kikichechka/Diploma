package com.example.taskplanner.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeNotes
import java.time.LocalDate

@Dao
interface NotesDao {
    @Query("SELECT * FROM note WHERE date = :date")
    suspend fun getNotesByDate(date: LocalDate): List<Note>

    @Query("SELECT * FROM reminder WHERE date = :date")
    suspend fun getRemindersByDate(date: LocalDate): List<Reminder>

    @Query("SELECT * FROM products WHERE date = :date")
    suspend fun getProductsByDate(date: LocalDate): List<Products>

    @Query("SELECT * FROM medications WHERE date = :date")
    suspend fun getMedicationsByDate(date: LocalDate): List<Medications>

    suspend fun getAllNotesByDate(date: LocalDate): MutableList<TypeNotes> {
        val list = mutableListOf<TypeNotes>().apply {
            addAll(
                getNotesByDate(date) + getRemindersByDate(date) + getMedicationsByDate(date) + getProductsByDate(date)
            )
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

    @Update(entity = Reminder::class)
    suspend fun updateReminder(reminder: Reminder)

    @Update(entity = Products::class)
    suspend fun updateProducts(products: Products)

    @Update(entity = Medications::class)
    suspend fun updateMedications(medications: Medications)

    @Update(entity = Note::class)
    suspend fun updateNote(note: Note)

    @Delete(entity = Note::class)
    suspend fun deleteNote(note: Note)

    @Delete(entity = Reminder::class)
    suspend fun deleteReminder(reminder: Reminder)

    @Delete(entity = Products::class)
    suspend fun deleteProducts(products: Products)

    @Delete(entity = Medications::class)
    suspend fun deleteMedications(medications: Medications)
}
