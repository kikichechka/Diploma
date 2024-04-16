package com.example.taskplanner.data.converters

import androidx.room.TypeConverter
import java.time.LocalTime

class ConvertersLocalTime {
    @TypeConverter
    fun fromLocalTime(localTime: LocalTime): String {
        return "$localTime"
    }

    @TypeConverter
    fun toLocation(str: String): LocalTime {
        val list = str.split(":")
        return LocalTime.of(list[0].toInt(), list[1].toInt())
    }
}
