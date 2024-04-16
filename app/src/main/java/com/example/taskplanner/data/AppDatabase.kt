package com.example.taskplanner.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskplanner.data.converters.ConvertersBoolean
import com.example.taskplanner.data.converters.ConvertersListLocalTime
import com.example.taskplanner.data.converters.ConvertersListString
import com.example.taskplanner.data.converters.ConvertersLocalDate
import com.example.taskplanner.data.converters.ConvertersLocalTime
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.Reminder

@Database(
    entities = [
        Note::class,
        Reminder::class,
        Products::class,
        Medications::class
    ],
    version = 4
)
@TypeConverters(
    ConvertersLocalDate::class,
    ConvertersLocalTime::class,
    ConvertersBoolean::class,
    ConvertersListString::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}
