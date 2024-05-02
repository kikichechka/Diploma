@file:Suppress("DEPRECATED_ANNOTATION")

package com.example.taskplanner.data.model.entity

import android.os.Parcelable
import java.time.LocalDate
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Day(var date: LocalDate, var list: MutableList<TypeTask> = mutableListOf()) :
    Parcelable {

    fun changeDate(year: Int, month: Int, day: Int) {
        val newDate = LocalDate.of(year, month, day)
        date = newDate
    }
}
