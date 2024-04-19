package com.example.taskplanner.data.model.repository

import android.util.Log
import com.example.taskplanner.data.NotesDao
import com.example.taskplanner.data.model.Day
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.ProductsWithList
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeNotes
import java.time.LocalDate
import javax.inject.Inject

class ListNoteRepository @Inject constructor(private val notesDao: NotesDao) {

    suspend fun getPageDays(firstCount: Int): List<Day> {
        var changeableCount = firstCount
        Log.d("@@@", "changeableCount =$changeableCount")
        return List(50) {
            val date = LocalDate.now().minusDays(25).plusDays(changeableCount++.toLong())
            val list = notesDao.getAllNotesByDate(date)
            Day(date, list)
        }
    }

    suspend fun deleteNote(note: TypeNotes) {
        when (note) {
            is Medications -> notesDao.deleteMedications(note)
            is Note -> notesDao.deleteNote(note)
            is ProductsWithList -> {
                notesDao.deleteProducts(note.productsName)
                note.listProducts!!.forEach {
                    notesDao.deleteProduct(it)
                }
            }

            is Reminder -> notesDao.deleteReminder(note)
        }
    }

    suspend fun changeFinishNote(note: TypeNotes) {
        when (note) {
            is Medications -> {
                note.finished = !note.finished
                notesDao.updateMedications(note)
            }

            is Note -> {
                note.finished = !note.finished
                notesDao.updateNote(note)
            }

            is ProductsWithList -> {
                note.productsName.finishedProducts = !note.productsName.finishedProducts
                notesDao.updateProducts(note.productsName)
                if (note.listProducts != null) {
                    note.listProducts.forEach {
                        it.finished = note.productsName.finishedProducts
                        notesDao.updateProduct(it)
                    }
                }
            }

            is Reminder -> {
                note.finished = !note.finished
                notesDao.updateReminder(note)
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
        val sizeFinishedProductList = productList.filter { p -> p.finished == false }.size

        if (sizeFinishedProductList == 0 && newProduct.finished == true) {
            val changeProducts = notesDao.getProductsById(newProduct.productsId)
            if (changeProducts.finishedProducts != newProduct.finished) {
                changeProducts.finishedProducts = newProduct.finished
                notesDao.updateProducts(changeProducts)
            }
        }
    }

    private suspend fun checkNotFinishedProducts(productList: List<Product>, newProduct: Product) {
        val sizeFinishedProductList = productList.filter { p -> p.finished == false }.size

        if (sizeFinishedProductList == 1 && newProduct.finished == false) {
            val changeProducts = notesDao.getProductsById(newProduct.productsId)
            if (changeProducts.finishedProducts != newProduct.finished) {
                changeProducts.finishedProducts = newProduct.finished
                notesDao.updateProducts(changeProducts)
            }
        }

    }

}
