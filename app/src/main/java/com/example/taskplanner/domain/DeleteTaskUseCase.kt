package com.example.taskplanner.domain

import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.ProductsWithList
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeTask
import com.example.taskplanner.data.model.repository.DeleteTaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val deleteTaskRepository: DeleteTaskRepository) {
    suspend fun deleteTask(task: TypeTask) {
        when (task) {
            is Medications -> {
                deleteTaskRepository.deleteMedications(task)
            }

            is Note -> deleteTaskRepository.deleteNote(task)
            is ProductsWithList -> {
                deleteTaskRepository.deleteProductsWithList(task)
            }

            is Reminder -> {
                deleteTaskRepository.deleteReminder(task)
            }
        }
    }
}
