package com.example.taskplanner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.taskplanner.data.Repository
import com.example.taskplanner.data.model.Day
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.TypeNotes
import com.example.taskplanner.view.adapter.CalendarPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class PlannerListDaysViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _pageDay = MutableStateFlow<Flow<PagingData<Day>>?>(null)
    val pageDay = _pageDay.asStateFlow()

    init {
        _pageDay.value = getPageDay()
    }

    private fun getPageDay(): Flow<PagingData<Day>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { CalendarPagingSource(repository) }
    ).flow.cachedIn(viewModelScope)

    suspend fun deleteNote(note: TypeNotes) {
        repository.deleteNote(note)
    }

    suspend fun changeFinishNote(note: TypeNotes) {
        repository.changeFinishNote(note)
    }
}
