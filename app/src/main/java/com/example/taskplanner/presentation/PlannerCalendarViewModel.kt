package com.example.taskplanner.presentation

import androidx.lifecycle.ViewModel
import com.example.taskplanner.data.model.Day
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.TypeTask
import com.example.taskplanner.data.model.repository.OneTaskRepository
import java.time.LocalDate
import javax.inject.Inject

class PlannerCalendarViewModel @Inject constructor(private val repository: OneTaskRepository) :
    ViewModel() {

    suspend fun getDay(date: LocalDate): Day {
        return repository.getDay(date)
    }

    suspend fun deleteTask(task: TypeTask) {
        repository.deleteTask(task)
    }

    suspend fun changeFinishTask(task: TypeTask) {
        repository.changeFinishTask(task)
    }

    suspend fun changeFinishProduct(product: Product) {
        repository.changeFinishProduct(product)
    }
}
