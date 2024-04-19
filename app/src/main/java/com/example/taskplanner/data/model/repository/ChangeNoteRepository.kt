package com.example.taskplanner.data.model.repository

import com.example.taskplanner.data.NotesDao
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.ProductsWithList
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeNotes
import javax.inject.Inject

class ChangeNoteRepository @Inject constructor(private val notesDao: NotesDao) {

    suspend fun changeNote(note: TypeNotes) {
        when(note) {
            is Medications -> notesDao.updateMedications(note)
            is Note -> notesDao.updateNote(note)
            is Reminder -> notesDao.updateReminder(note)
            is ProductsWithList -> {
                if (note.listProducts!=null) {
                    note.listProducts.forEach {
                        notesDao.insertProduct(it)
                    }
                }
            }
        }
    }

    suspend fun deleteOldListProduct(listProduct: List<Product>) {
        listProduct.forEach {
            notesDao.deleteProduct(it)
        }
    }
}
