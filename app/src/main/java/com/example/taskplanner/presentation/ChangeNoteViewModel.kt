package com.example.taskplanner.presentation

import androidx.lifecycle.ViewModel
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.ProductsWithList
import com.example.taskplanner.data.model.repository.ChangeNoteRepository
import com.example.taskplanner.data.model.entity.TypeNotes
import javax.inject.Inject

class ChangeNoteViewModel @Inject constructor(
    private val repository: ChangeNoteRepository
) : ViewModel() {

    suspend fun saveNote(note: TypeNotes) {
        repository.changeNote(note)
    }

    suspend fun changeProducts(productsWithList: ProductsWithList, newListProduct: List<Product>) {
        if (productsWithList.listProducts!=null) {
            deleteOldListProduct(productsWithList.listProducts.toList())
            productsWithList.listProducts.clear()
            productsWithList.listProducts.addAll(newListProduct)
        }
        saveNote(productsWithList)
    }

    private suspend fun deleteOldListProduct(listProduct: List<Product>) {
        repository.deleteOldListProduct(listProduct)
    }
}
