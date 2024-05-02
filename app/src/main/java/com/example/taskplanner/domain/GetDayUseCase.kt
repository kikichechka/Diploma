package com.example.taskplanner.domain

import com.example.taskplanner.data.model.entity.Day
import com.example.taskplanner.data.model.repository.GetTaskRepository
import java.time.LocalDate
import javax.inject.Inject

class GetDayUseCase @Inject constructor(private val getTaskRepository: GetTaskRepository) {

    suspend fun getDay(date: LocalDate): Day {
        val list = getTaskRepository.getAllNotesByDate(date)
        Day(date, list)
        return Day(date, list)
    }
}
