package com.example.taskplanner.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "note")
class Note(
    @PrimaryKey(autoGenerate = true) override var id: Int? = null,
    @ColumnInfo(name = "date") override var date: LocalDate,
    @ColumnInfo(name = "title") override var title: String,
    @ColumnInfo(name = "finished") override var finished: Boolean = false,
) : TypeTask(id, date, title, finished)
