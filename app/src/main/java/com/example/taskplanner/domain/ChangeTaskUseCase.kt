package com.example.taskplanner.domain

import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeTask
import com.example.taskplanner.data.model.repository.UpdateTaskRepository
import javax.inject.Inject

class ChangeTaskUseCase @Inject constructor(
    private val updateTaskRepository: UpdateTaskRepository
) {
    suspend fun changeTask(task: TypeTask) {
        when (task) {
            is Medications -> {
                updateTaskRepository.updateMedications(task)
            }

            is Note -> updateTaskRepository.updateNote(task)
            is Reminder -> {
                updateTaskRepository.updateReminder(task)
            }

            else -> {}
        }
    }
}
