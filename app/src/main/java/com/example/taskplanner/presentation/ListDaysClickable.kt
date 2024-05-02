package com.example.taskplanner.presentation

import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.TypeTask

interface ListDaysClickable {
    fun deleteTaskClick(task: TypeTask)
    fun changeTaskClick(task: TypeTask)
    fun changeFinishTask(task: TypeTask)
    fun changeFinishProduct(product: Product)
}
