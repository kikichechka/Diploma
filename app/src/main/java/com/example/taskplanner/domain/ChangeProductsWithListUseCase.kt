package com.example.taskplanner.domain

import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.ProductsWithList
import com.example.taskplanner.data.model.repository.AddTaskRepository
import com.example.taskplanner.data.model.repository.DeleteTaskRepository
import com.example.taskplanner.data.model.repository.GetTaskRepository
import com.example.taskplanner.data.model.repository.UpdateTaskRepository
import javax.inject.Inject

class ChangeProductsWithListUseCase @Inject constructor(
    private val updateTaskRepository: UpdateTaskRepository,
    private val getTaskRepository: GetTaskRepository,
    private val deleteTaskRepository: DeleteTaskRepository,
    private val addTaskRepository: AddTaskRepository
) {
    suspend fun changeProductList(
        productsWithList: ProductsWithList,
        newListTitle: MutableList<String>,
    ) {
        val products = productsWithList.productsName
        products.dateProducts = productsWithList.date
        updateTaskRepository.updateProducts(products)

        if (productsWithList.listProducts != null) {
            deleteOldListProduct(productsWithList.listProducts.toList())
            val newListProduct = mutableListOf<Product>()

            productsWithList.listProducts.forEach { prod ->
                if (newListTitle.contains(prod.title)) {
                    newListProduct.add(prod)
                    newListTitle.remove(prod.title)
                }
            }

            newListTitle.forEach { title ->
                newListProduct.add(
                    Product(
                        productsId = productsWithList.productsName.idProducts ?: 0,
                        id = null,
                        title = title
                    )
                )
            }
            productsWithList.listProducts.clear()
            productsWithList.listProducts.addAll(newListProduct)
            productsWithList.listProducts.forEach {
                addTaskRepository.addProduct(it)
            }
            checkFinishedProducts(productsWithList.listProducts)
        }
    }

    private suspend fun deleteOldListProduct(listProduct: List<Product>) {
        listProduct.forEach {
            deleteTaskRepository.deleteProduct(it)
        }
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
