package com.example.taskplanner.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.example.taskplanner.domain.Day
import com.example.taskplanner.domain.MyCalendar
import com.example.taskplanner.model.CalendarPagingSource
import com.example.taskplanner.model.Repository
import com.example.taskplanner.model.RepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlannerViewModel @Inject constructor(
//    private val liveData: MutableLiveData<MyCalendar> = MutableLiveData<MyCalendar>(),
//    private val repository: Repository = RepositoryImpl(),
    private val calendarPagingSource: CalendarPagingSource
) : ViewModel() {

    val pageDay: Flow<PagingData<Day>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { calendarPagingSource }
    ).flow.cachedIn(viewModelScope)

//    fun getLiveData(): MutableLiveData<MyCalendar> {
//        return liveData
//    }
//
//    fun sentRequest() {
//        liveData.value = repository.getCalendar()
//    }
}
