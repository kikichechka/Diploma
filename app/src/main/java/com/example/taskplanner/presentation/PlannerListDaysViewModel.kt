package com.example.taskplanner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.taskplanner.data.model.repository.ListNoteRepository
import com.example.taskplanner.data.model.Day
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.TypeNotes
import com.example.taskplanner.view.adapter.CalendarPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlannerListDaysViewModel @Inject constructor(
    private val listNoteRepository: ListNoteRepository
) : ViewModel() {

    val getPageDay: Flow<PagingData<Day>> = Pager(
        config = PagingConfig(pageSize = 40),
        pagingSourceFactory = { CalendarPagingSource(listNoteRepository) }
    ).flow.cachedIn(viewModelScope)

    suspend fun deleteNote(note: TypeNotes) {
        listNoteRepository.deleteNote(note)
    }

    suspend fun changeFinishNote(note: TypeNotes) {
        listNoteRepository.changeFinishNote(note)
    }

    suspend fun changeFinishProduct(product: Product) {
        listNoteRepository.changeFinishProduct(product)
    }
}
