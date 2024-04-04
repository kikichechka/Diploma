package com.example.taskplanner.model

import android.util.Log
import com.example.taskplanner.domain.Day
import com.example.taskplanner.domain.MyCalendar
import java.time.LocalDate
import javax.inject.Inject

class RepositoryImpl @Inject constructor() : Repository {
    private val calendar = MyCalendar()

    override fun getCalendar(): MyCalendar {
        return calendar
    }

    fun getPageDays(firstCount: Int): List<Day> {
        var changeableCount = firstCount
        return List(10) {
//            Log.d("@@@", "change count =$count")
            Day(LocalDate.now().plusDays(changeableCount++.toLong()))
        }
//        return myCalendar
    }
}