package com.example.taskplanner.presentation.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.taskplanner.data.model.entity.Day
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeTask
import com.example.taskplanner.data.model.IntentManager
import com.example.taskplanner.domain.ChangeFinishProductUseCase
import com.example.taskplanner.domain.ChangeFinishTaskUseCase
import com.example.taskplanner.domain.DeleteTaskUseCase
import com.example.taskplanner.domain.GetDayUseCase
import java.time.LocalDate
import javax.inject.Inject

class PlannerCalendarViewModel @Inject constructor(
    private val getDayUseCase: GetDayUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val changeFinishTaskUseCase: ChangeFinishTaskUseCase,
    private val changeFinishProductUseCase: ChangeFinishProductUseCase
) :
    ViewModel() {

    suspend fun getDay(date: LocalDate): Day {
        return getDayUseCase.getDay(date)
    }

    suspend fun deleteTask(task: TypeTask, context: Context) {
        deleteTaskUseCase.deleteTask(task)
        when (task) {
            is Medications -> IntentManager.cancelMedicationIntent(task, context)
            is Reminder -> IntentManager.cancelReminderIntent(task, context)
            else -> {}
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    suspend fun changeFinishTask(task: TypeTask, context: Context) {
        changeFinishTaskUseCase.changeFinishTask(task)
        when (task) {
            is Medications -> IntentManager.stopOrResumeMedicationIntent(task, context)
            is Reminder -> IntentManager.stopOrResumeReminderIntent(task, context)
            else -> {}
        }
    }

    suspend fun changeFinishProduct(product: Product) {
        changeFinishProductUseCase.changeFinishProduct(product)
    }
}
