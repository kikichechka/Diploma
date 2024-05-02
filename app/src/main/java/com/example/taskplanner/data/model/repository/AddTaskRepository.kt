package com.example.taskplanner.data.model.repository

import com.example.taskplanner.data.TasksDao
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.Reminder
import javax.inject.Inject

class AddTaskRepository @Inject constructor(private val tasksDao: TasksDao) {

    suspend fun addNote(note: Note) {
        tasksDao.insertNote(note)
    }

    suspend fun addMedications(medications: Medications) {
        tasksDao.insertMedications(medications)
    }

    suspend fun addReminder(reminder: Reminder) {
        tasksDao.insertReminder(reminder)
    }

    suspend fun addProducts(products: Products) {
        tasksDao.insertProducts(products)
    }

    suspend fun addProduct(product: Product) {
        tasksDao.insertProduct(product)
    }
}
