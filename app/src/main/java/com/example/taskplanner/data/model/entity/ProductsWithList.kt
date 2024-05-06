package com.example.taskplanner.data.model.entity

import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.RawValue

data class ProductsWithList(
    @Embedded
    val productsName: @RawValue Products,
    @Relation(
        parentColumn = "id_products",
        entityColumn = "product_id"
    )
    val listProducts: @RawValue MutableList<Product>? = null
) : TypeTask(
    productsName.idProducts,
    productsName.dateProducts,
    productsName.titleProducts,
    productsName.finishedProducts
)
