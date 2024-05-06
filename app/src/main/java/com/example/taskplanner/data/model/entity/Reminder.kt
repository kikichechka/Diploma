package com.example.taskplanner.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "reminder")
class Reminder(
    @PrimaryKey(autoGenerate = true) override var id: Int? = null,
    @ColumnInfo(name = "date") override var date: LocalDate,
    @ColumnInfo(name = "title") override var title: String,
    @ColumnInfo(name = "finished") override var finished: Boolean = false,
    @ColumnInfo(name = "time") var time: LocalTime
) : TypeTask(id, date, title, finished)
