@file:Suppress("DEPRECATED_ANNOTATION")

package com.example.taskplanner.data.model.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import java.time.LocalTime

@Parcelize
sealed class TypeNotes (
    open var id: Int?,
    open val date: LocalDate,
    open var title: String,
    open var finished: Boolean
): Parcelable

@Entity(tableName = "note")
class Note(
    @PrimaryKey(autoGenerate = true) override var id: Int? = null,
    @ColumnInfo(name = "date") override var date: LocalDate,
    @ColumnInfo(name = "title") override var title: String,
    @ColumnInfo(name = "finished") override var finished: Boolean = false,
) : TypeNotes(id, date, title, finished)

@Entity(tableName = "reminder")
class Reminder(
    @PrimaryKey(autoGenerate = true) override var id: Int? = null,
    @ColumnInfo(name = "date") override var date: LocalDate,
    @ColumnInfo(name = "title") override var title: String,
    @ColumnInfo(name = "finished") override var finished: Boolean = false,
    @ColumnInfo(name = "time") var time: LocalTime
) : TypeNotes(id, date, title, finished)

@Entity(tableName = "products")
class Products(
    @PrimaryKey(autoGenerate = true) override var id: Int? = null,
    @ColumnInfo(name = "date") override var date: LocalDate,
    @ColumnInfo(name = "title") override var title: String,
    @ColumnInfo(name = "finished") override var finished: Boolean = false,
    @ColumnInfo(name = "listProducts") val listProducts: MutableList<String>
) : TypeNotes(id, date, title, finished)

@Entity(tableName = "medications")
class Medications(
    @PrimaryKey(autoGenerate = true) override var id: Int? = null,
    @ColumnInfo(name = "date") override var date: LocalDate,
    @ColumnInfo(name = "title") override var title: String,
    @ColumnInfo(name = "finished") override var finished: Boolean = false,
    @ColumnInfo(name = "time") var time: LocalTime,
    @ColumnInfo(name = "count_taking_medication") val countTakingMedication: MutableList<LocalTime>
) : TypeNotes(id, date, title, finished)
