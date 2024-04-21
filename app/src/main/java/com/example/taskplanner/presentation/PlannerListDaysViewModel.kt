package com.example.taskplanner.presentation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.taskplanner.data.model.repository.ListTaskRepository
import com.example.taskplanner.data.model.Day
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.TypeTask
import com.example.taskplanner.view.adapter.CalendarPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlannerListDaysViewModel @Inject constructor(
    private val listTaskRepository: ListTaskRepository
) : ViewModel() {

    val getPageDay: Flow<PagingData<Day>> = Pager(
        config = PagingConfig(pageSize = 40),
        pagingSourceFactory = { CalendarPagingSource(listTaskRepository) }
    ).flow.cachedIn(viewModelScope)

    suspend fun deleteTask(task: TypeTask, context: Context) {
        listTaskRepository.deleteTask(task, context)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    suspend fun changeFinishTask(task: TypeTask, context: Context) {
        listTaskRepository.changeFinishTask(task, context)
    }

    suspend fun changeFinishProduct(product: Product) {
        listTaskRepository.changeFinishProduct(product)
    }
}
