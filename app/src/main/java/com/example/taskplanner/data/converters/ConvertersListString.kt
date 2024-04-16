package com.example.taskplanner.data.converters

import androidx.room.TypeConverter

class ConvertersListString {
    @TypeConverter
    fun fromLocalDate(list: List<String>): String {
        return list.joinToString(separator = "~")
    }

    @TypeConverter
    fun toLocation(str: String): List<String> {
        return str.split("~")
    }
}
