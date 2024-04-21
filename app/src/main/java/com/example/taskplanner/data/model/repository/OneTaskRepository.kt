package com.example.taskplanner.data.model.repository

import com.example.taskplanner.data.NotesDao
import com.example.taskplanner.data.model.Day
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.ProductsWithList
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeTask
import java.time.LocalDate
import javax.inject.Inject

class OneTaskRepository @Inject constructor(private val notesDao: NotesDao) {

    suspend fun getDay(date: LocalDate) : Day {
        val list = notesDao.getAllNotesByDate(date)
        Day(date, list)
        return Day(date, list)
    }

    suspend fun deleteTask(task: TypeTask) {
        when(task) {
            is Medications -> notesDao.deleteMedications(task)
            is Note -> notesDao.deleteNote(task)
            is ProductsWithList -> {
                notesDao.deleteProducts(task.productsName)
                if (task.listProducts!=null) {
                    task.listProducts.forEach {
                        notesDao.deleteProduct(it)
                    }
                }
            }
            is Reminder -> notesDao.deleteReminder(task)
        }
    }

    suspend fun changeFinishTask(task: TypeTask) {
        when (task) {
            is Medications -> {
                task.finished = !task.finished
                notesDao.updateMedications(task)
            }
            is Note -> {
                task.finished = !task.finished
                notesDao.updateNote(task)
            }
            is ProductsWithList -> {
                task.productsName.finishedProducts = !task.productsName.finishedProducts
                notesDao.updateProducts(task.productsName)
                if (task.listProducts != null) {
                    task.listProducts.forEach {
                        it.finished = task.productsName.finishedProducts
                        notesDao.updateProduct(it)
                    }
                }
            }
            is Reminder -> {
                task.finished = !task.finished
                notesDao.updateReminder(task)
            }
        }
    }

    suspend fun changeFinishProduct(product: Product) {
        val newProduct = product.copy(finished = !product.finished)
        notesDao.updateProduct(newProduct)

        val productList = notesDao.getListProduct(newProduct.productsId)
        checkFinishedProducts(productList, newProduct)
        checkNotFinishedProducts(productList, newProduct)
    }

    private suspend fun checkFinishedProducts(productList: List<Product>, newProduct: Product) {
        val sizeFinishedProductList = productList.filter { p -> !p.finished }.size

        if (sizeFinishedProductList == 0 && newProduct.finished) {
            val changeProducts = notesDao.getProductsById(newProduct.productsId)
            if (changeProducts.finishedProducts != newProduct.finished) {
                changeProducts.finishedProducts = newProduct.finished
                notesDao.updateProducts(changeProducts)
            }
        }
    }

    private suspend fun checkNotFinishedProducts(productList: List<Product>, newProduct: Product) {
        val sizeFinishedProductList = productList.filter { p -> !p.finished }.size

        if (sizeFinishedProductList == 1 && !newProduct.finished) {
            val changeProducts = notesDao.getProductsById(newProduct.productsId)
            if (changeProducts.finishedProducts != newProduct.finished) {
                changeProducts.finishedProducts = newProduct.finished
                notesDao.updateProducts(changeProducts)
            }
        }
    }
}
