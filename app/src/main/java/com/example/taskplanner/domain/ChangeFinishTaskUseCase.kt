package com.example.taskplanner.domain

import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.ProductsWithList
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeTask
import com.example.taskplanner.data.model.repository.UpdateTaskRepository
import javax.inject.Inject

class ChangeFinishTaskUseCase @Inject constructor(private val updateTaskRepository: UpdateTaskRepository) {
    suspend fun changeFinishTask(task: TypeTask) {
        when (task) {
            is Medications -> {
                task.finished = !task.finished
                updateTaskRepository.updateMedications(task)
            }

            is Note -> {
                task.finished = !task.finished
                updateTaskRepository.updateNote(task)
            }

            is ProductsWithList -> {
                task.productsName.finishedProducts = !task.productsName.finishedProducts
                updateTaskRepository.updateProducts(task.productsName)
                if (task.listProducts != null) {
                    task.listProducts.forEach {
                        it.finished = task.productsName.finishedProducts
                        updateTaskRepository.updateProduct(it)
                    }
                }
            }

            is Reminder -> {
                task.finished = !task.finished
                updateTaskRepository.updateReminder(task)
            }
        }
    }
}
