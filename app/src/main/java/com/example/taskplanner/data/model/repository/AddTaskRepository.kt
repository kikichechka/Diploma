package com.example.taskplanner.data.model.repository

import com.example.taskplanner.data.NotesDao
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeTask
import javax.inject.Inject

class AddTaskRepository @Inject constructor(private val notesDao: NotesDao) {

    suspend fun addTask(task: TypeTask) {
        when(task) {
            is Medications -> notesDao.insertMedications(task)
            is Note -> notesDao.insertNote(task)
            is Reminder -> notesDao.insertReminder(task)
            else -> {}
        }
    }

    suspend fun addProducts(products: Products, list: List<String>) {
        notesDao.insertProducts(products)

        notesDao.getProducts(products.dateProducts).last()
        list.forEach {
                notesDao.insertProduct(Product(productsId = products.idProducts!!, title = it))
        }
    }
}
