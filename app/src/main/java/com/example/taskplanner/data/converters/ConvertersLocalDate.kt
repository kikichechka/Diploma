package com.example.taskplanner.data.converters

import androidx.room.TypeConverter
import java.time.LocalDate

class ConvertersLocalDate {
    @TypeConverter
    fun fromLocalDate(localDate: LocalDate): String {
        return "$localDate"
    }

    @TypeConverter
    fun toLocation(str: String): LocalDate {
        val list = str.split("-")
        return LocalDate.of(list[0].toInt(), list[1].toInt(), list[2].toInt())
    }
}
