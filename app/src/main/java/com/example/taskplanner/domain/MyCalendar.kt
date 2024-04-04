package com.example.taskplanner.domain

import java.time.LocalDate
import java.time.LocalTime

class MyCalendar(private val calendar: MutableList<Day> = mutableListOf()) {

    fun getCalendar(): List<Day> {
        val myCalendar = calendar
        myCalendar.clear()
        myCalendar.add(Day(LocalDate.now()))
        myCalendar.add(Day(LocalDate.now().plusDays(1)))
        myCalendar.add(Day(LocalDate.now().plusDays(2)))
        myCalendar.add(Day(LocalDate.now().plusDays(3)))
        myCalendar.add(Day(LocalDate.now().plusDays(4)))
        myCalendar.add(Day(LocalDate.now().plusDays(5)))
        myCalendar.add(Day(LocalDate.now().plusDays(6)))
        myCalendar[1].list.add(TypeNotes.Note("title", true))
        myCalendar[1].list.add(TypeNotes.ListOfProducts("title"))
        myCalendar[2].list.add(TypeNotes.Reminder("titlee", LocalTime.now()))
        myCalendar[2].list.add(TypeNotes.Note("title", true))
        myCalendar[2].list.add(TypeNotes.ListOfProducts("title"))

        return myCalendar.toList()
    }
}
