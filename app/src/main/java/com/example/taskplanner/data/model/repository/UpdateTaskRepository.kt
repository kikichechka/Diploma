package com.example.taskplanner.data.model.repository

import com.example.taskplanner.data.TasksDao
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.Reminder
import javax.inject.Inject

class UpdateTaskRepository @Inject constructor(private val tasksDao: TasksDao) {

    suspend fun updateNote(note: Note) {
        tasksDao.updateNote(note)
    }

    suspend fun updateReminder(reminder: Reminder) {
        tasksDao.updateReminder(reminder)
    }

    suspend fun updateMedications(medications: Medications) {
        tasksDao.updateMedications(medications)
    }

    suspend fun updateProducts(products: Products) {
        tasksDao.updateProducts(products)
    }

    suspend fun updateProduct(product: Product) {
        tasksDao.updateProduct(product)
    }
}
