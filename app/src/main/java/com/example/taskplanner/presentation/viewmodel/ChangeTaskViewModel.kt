package com.example.taskplanner.presentation.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.ProductsWithList
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeTask
import com.example.taskplanner.data.model.IntentManager
import com.example.taskplanner.domain.ChangeProductsWithListUseCase
import com.example.taskplanner.domain.ChangeTaskUseCase
import javax.inject.Inject

class ChangeTaskViewModel @Inject constructor(
    private val changeTaskUseCase: ChangeTaskUseCase,
    private val changeProductsWithListUseCase: ChangeProductsWithListUseCase
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.S)
    suspend fun changeTask(task: TypeTask, context: Context) {
        changeTaskUseCase.changeTask(task)
        when (task) {
            is Medications -> IntentManager.stopOrResumeMedicationIntent(task, context)
            is Reminder -> IntentManager.stopOrResumeReminderIntent(task, context)
            else -> {}
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    suspend fun changeProductsWithList(
        productsWithList: ProductsWithList,
        newListProduct: MutableList<String>
    ) {
        changeProductsWithListUseCase.changeProductList(productsWithList, newListProduct)
    }
}
