package com.example.taskplanner.presentation.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.taskplanner.data.model.entity.Day
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeTask
import com.example.taskplanner.data.model.IntentManager
import com.example.taskplanner.domain.ChangeFinishProductUseCase
import com.example.taskplanner.domain.ChangeFinishTaskUseCase
import com.example.taskplanner.domain.DeleteTaskUseCase
import com.example.taskplanner.domain.GetPageDaysUseCase
import com.example.taskplanner.presentation.adapters.CalendarPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlannerListDaysViewModel @Inject constructor(
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getPageDaysUseCase: GetPageDaysUseCase,
    private val changeFinishTaskUseCase: ChangeFinishTaskUseCase,
    private val changeFinishProductUseCase: ChangeFinishProductUseCase
) : ViewModel() {

    val getPageDay: Flow<PagingData<Day>> = Pager(
        config = PagingConfig(pageSize = 40),
        pagingSourceFactory = { CalendarPagingSource(getPageDaysUseCase) }
    ).flow.cachedIn(viewModelScope)


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
            is Medications -> IntentManager.stopOrResumeMedicationIntent(
                medications = task,
                context = context
            )

            is Reminder -> IntentManager.stopOrResumeReminderIntent(
                reminder = task,
                context = context
            )

            else -> {}
        }
    }

    suspend fun changeFinishProduct(product: Product) {
        changeFinishProductUseCase.changeFinishProduct(product)
    }
}
