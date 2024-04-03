package com.example.taskplanner.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalTime

@Parcelize
sealed class TypeNotes : Parcelable {
    abstract var title: String
    abstract var finished: Boolean

    class Note(override var title: String, override var finished: Boolean = false) : TypeNotes()
    class Reminder(
        override var title: String,
        var time: LocalTime,
        override var finished: Boolean = false
    ) : TypeNotes()

    class ListOfProducts(
        override var title: String,
        override var finished: Boolean = false,
        val listProducts: MutableList<String> = mutableListOf()
    ) : TypeNotes()
}