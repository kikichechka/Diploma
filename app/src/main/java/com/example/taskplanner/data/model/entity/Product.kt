package com.example.taskplanner.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "product_id")
    val productsId: Int,
    @ColumnInfo(name = "finished")
    var finished: Boolean = false,
    @ColumnInfo(name = "title")
    var title: String
)
