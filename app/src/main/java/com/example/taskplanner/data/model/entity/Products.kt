package com.example.taskplanner.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "products")
class Products(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_products")
    var idProducts: Int? = null,
    @ColumnInfo(name = "date_products")
    var dateProducts: LocalDate,
    @ColumnInfo(name = "title_products")
    var titleProducts: String,
    @ColumnInfo(name = "finished_products")
    var finishedProducts: Boolean = false,
)
