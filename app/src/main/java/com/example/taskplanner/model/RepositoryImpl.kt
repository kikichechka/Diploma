package com.example.taskplanner.model

import com.example.taskplanner.domain.MyCalendar

class RepositoryImpl: Repository {
    private val calendar = MyCalendar()

    override fun getCalendar(): MyCalendar {
        return calendar
    }
}