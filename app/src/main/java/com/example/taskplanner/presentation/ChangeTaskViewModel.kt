package com.example.taskplanner.presentation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.ProductsWithList
import com.example.taskplanner.data.model.repository.ChangeTaskRepository
import com.example.taskplanner.data.model.entity.TypeTask
import javax.inject.Inject

class ChangeTaskViewModel @Inject constructor(
    private val repository: ChangeTaskRepository
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.S)
    suspend fun saveTask(task: TypeTask, context: Context) {
        repository.changeTask(task, context)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    suspend fun changeProducts(productsWithList: ProductsWithList, newListProduct: List<Product>, context: Context) {
        if (productsWithList.listProducts!=null) {
            deleteOldListProduct(productsWithList.listProducts.toList())
            productsWithList.listProducts.clear()
            productsWithList.listProducts.addAll(newListProduct)
        }
        saveTask(productsWithList, context)
    }

    private suspend fun deleteOldListProduct(listProduct: List<Product>) {
        repository.deleteOldListProduct(listProduct)
    }
}
