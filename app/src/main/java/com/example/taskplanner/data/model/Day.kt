@file:Suppress("DEPRECATED_ANNOTATION")

package com.example.taskplanner.data.model

import android.os.Parcelable
import com.example.taskplanner.data.model.entity.TypeNotes
import java.time.LocalDate
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Day(var date: LocalDate, var list: MutableList<TypeNotes> = mutableListOf()) :
    Parcelable {

    fun changeDate(year: Int, month: Int, day: Int) {
        val newDate = LocalDate.of(year, month, day)
        date = newDate
    }
}
