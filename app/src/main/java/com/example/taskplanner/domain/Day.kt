package com.example.taskplanner.domain

import android.os.Parcelable
import java.time.LocalDate
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Day(var date: LocalDate, val list: MutableList<TypeNotes> = mutableListOf()) :
    Parcelable {
    override fun toString(): String {
        return String.format("%02d.%02d.%d", date.dayOfMonth, date.monthValue, date.year)
    }

    fun changeDate(year:Int, month:Int, day:Int){
        val newDate = LocalDate.of(year, month, day)
        date = newDate
    }
}
