@file:Suppress("DEPRECATED_ANNOTATION")

package com.example.taskplanner.data.model.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.time.LocalDate
import java.time.LocalTime

@Parcelize
sealed class TypeTask(
    open var id: Int?,
    open var date: LocalDate,
    open var title: String,
    open var finished: Boolean
) : Parcelable

@Entity(tableName = "note")
class Note(
    @PrimaryKey(autoGenerate = true) override var id: Int? = null,
    @ColumnInfo(name = "date") override var date: LocalDate,
    @ColumnInfo(name = "title") override var title: String,
    @ColumnInfo(name = "finished") override var finished: Boolean = false,
) : TypeTask(id, date, title, finished)

@Entity(tableName = "reminder")
class Reminder(
    @PrimaryKey(autoGenerate = true) override var id: Int? = null,
    @ColumnInfo(name = "date") override var date: LocalDate,
    @ColumnInfo(name = "title") override var title: String,
    @ColumnInfo(name = "finished") override var finished: Boolean = false,
    @ColumnInfo(name = "time") var time: LocalTime
) : TypeTask(id, date, title, finished)

@Entity(tableName = "medications")
class Medications(
    @PrimaryKey(autoGenerate = true)
    override var id: Int? = null,
    @ColumnInfo(name = "date")
    override var date: LocalDate,
    @ColumnInfo(name = "title")
    override var title: String,
    @ColumnInfo(name = "finished")
    override var finished: Boolean = false,
    @ColumnInfo(name = "time")
    var time: LocalTime
) : TypeTask(id, date, title, finished)

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
