package com.example.taskplanner.domain

import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeTask
import com.example.taskplanner.data.model.repository.AddTaskRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(private val addTaskRepository: AddTaskRepository) {
    suspend fun addTask(task: TypeTask) {
        when (task) {
            is Medications -> addTaskRepository.addMedications(task)
            is Note -> addTaskRepository.addNote(task)
            is Reminder -> addTaskRepository.addReminder(task)
            else -> {}
        }
    }
}
