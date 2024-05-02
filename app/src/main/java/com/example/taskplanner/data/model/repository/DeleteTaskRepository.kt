package com.example.taskplanner.data.model.repository

import com.example.taskplanner.data.TasksDao
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.ProductsWithList
import com.example.taskplanner.data.model.entity.Reminder
import javax.inject.Inject

class DeleteTaskRepository @Inject constructor(private val tasksDao: TasksDao) {

    suspend fun deleteNote(note: Note) {
        tasksDao.deleteNote(note)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        tasksDao.deleteReminder(reminder)
    }

    suspend fun deleteMedications(medications: Medications) {
        tasksDao.deleteMedications(medications)
    }

    suspend fun deleteProductsWithList(productsWithList: ProductsWithList) {
        tasksDao.deleteProducts(productsWithList.productsName)
        if (productsWithList.listProducts != null) {
            productsWithList.listProducts.forEach {
                deleteProduct(it)
            }
        }
    }

    suspend fun deleteProduct(product: Product) {
        tasksDao.deleteProduct(product)
    }
}
