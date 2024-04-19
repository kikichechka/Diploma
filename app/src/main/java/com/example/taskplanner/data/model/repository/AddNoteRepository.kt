package com.example.taskplanner.data.model.repository

import com.example.taskplanner.data.NotesDao
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.ProductsWithList
import com.example.taskplanner.data.model.entity.Reminder
import javax.inject.Inject

class AddNoteRepository @Inject constructor(private val notesDao: NotesDao) {

    suspend fun addNote(note: Note) {
        notesDao.insertNote(note)
    }

    suspend fun addReminder(reminder: Reminder) {
        notesDao.insertReminder(reminder)
    }

    suspend fun addProducts(products: Products, list: List<String>) {
        notesDao.insertProducts(products)

        val products = notesDao.getProducts(products.dateProducts).last()
        list.forEach {
            notesDao.insertProduct(Product(productsId = products.idProducts!!, title = it))
        }
    }

    suspend fun addMedications(medications: Medications) {
        notesDao.insertMedications(medications)
    }
}
