package com.example.taskplanner.model

import com.example.taskplanner.domain.MyCalendar

fun interface Repository {
    fun getCalendar(): MyCalendar
}