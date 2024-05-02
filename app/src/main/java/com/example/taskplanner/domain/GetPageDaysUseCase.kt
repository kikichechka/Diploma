package com.example.taskplanner.domain

import com.example.taskplanner.data.model.entity.Day
import com.example.taskplanner.data.model.repository.GetTaskRepository
import java.time.LocalDate
import javax.inject.Inject

class GetPageDaysUseCase @Inject constructor(private val getTaskRepository: GetTaskRepository) {
    suspend fun getPageDays(firstCount: Int): List<Day> {
        var changeableCount = firstCount
        return List(50) {
            val date = LocalDate.now().minusDays(25)
                .plusDays(changeableCount++.toLong())
            val list = getTaskRepository.getAllNotesByDate(date)
            Day(date, list)
        }
    }
}
