package com.example.taskplanner.view

import com.example.taskplanner.domain.Day

fun interface OnItemClickDay {
    fun onDayClick(day: Day)
}