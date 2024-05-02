package com.example.taskplanner.domain

import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.repository.AddTaskRepository
import com.example.taskplanner.data.model.repository.GetTaskRepository
import javax.inject.Inject

class AddProductsWithListUseCase @Inject constructor(
    private val addTaskRepository: AddTaskRepository,
    private val getTaskRepository: GetTaskRepository
) {
    suspend fun addProductsWithList(products: Products, list: List<String>) {
        addTaskRepository.addProducts(products)
        val idProducts = getTaskRepository.getLastProducts(products.dateProducts).idProducts ?: 0
        list.forEach {
            addTaskRepository.addProduct(Product(productsId = idProducts, title = it))
        }
    }
}
