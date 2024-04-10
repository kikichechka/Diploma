package com.example.taskplanner.data.converters

import androidx.room.TypeConverter
import java.time.LocalTime

class ConvertersListLocalTime {
    @TypeConverter
    fun fromLocalTime(list: List<LocalTime>): String {
        return list.joinToString()
    }

    @TypeConverter
    fun toLocation(str: String): List<LocalTime> {
        val listStr = str.split(",")
        val listLocalTime = mutableListOf<LocalTime>()
        listStr.forEach { str ->
            val time = str.split(":")
            listLocalTime.add(LocalTime.of(time[0].toInt(), time[1].toInt(), time[2].toInt()))
        }
        return listLocalTime.toList()
    }
}
