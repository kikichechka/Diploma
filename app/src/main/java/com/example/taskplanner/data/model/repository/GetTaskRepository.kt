package com.example.taskplanner.data.model.repository

import com.example.taskplanner.data.TasksDao
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.TypeTask
import java.time.LocalDate
import javax.inject.Inject

class GetTaskRepository @Inject constructor(private val tasksDao: TasksDao) {
    suspend fun getAllNotesByDate(date: LocalDate): MutableList<TypeTask> {
        return tasksDao.getAllNotesByDate(date)
    }

    suspend fun getListProduct(productsId: Int): List<Product> {
        return tasksDao.getListProduct(productsId)
    }

    suspend fun getProductsById(productsId: Int): Products {
        return tasksDao.getProductsById(productsId)
    }

    suspend fun getLastProducts(date: LocalDate): Products {
        return tasksDao.getProducts(date).last()
    }
}
