package com.example.taskplanner.domain

import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.repository.GetTaskRepository
import com.example.taskplanner.data.model.repository.UpdateTaskRepository
import javax.inject.Inject

class ChangeFinishProductUseCase @Inject constructor(
    private val updateTaskRepository: UpdateTaskRepository,
    private val getTaskRepository: GetTaskRepository
) {

    suspend fun changeFinishProduct(product: Product) {
        product.finished = !product.finished
        updateTaskRepository.updateProduct(product)
        val productList = getTaskRepository.getListProduct(product.productsId)
        checkFinishedProducts(productList)
    }

    private suspend fun checkFinishedProducts(productList: List<Product>) {
        val sizeFinishedProductList = productList.filter { p -> !p.finished }.size
        val products = getTaskRepository.getProductsById(productList.first().productsId)

        if (sizeFinishedProductList == 0 && !products.finishedProducts) {
            products.finishedProducts = true
            updateTaskRepository.updateProducts(products)
        }

        if (sizeFinishedProductList > 0 && products.finishedProducts) {
            products.finishedProducts = false
            updateTaskRepository.updateProducts(products)
        }
    }
}