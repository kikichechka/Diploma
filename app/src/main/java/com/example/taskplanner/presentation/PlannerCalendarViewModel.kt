package com.example.taskplanner.presentation

import androidx.lifecycle.ViewModel
import com.example.taskplanner.data.model.Day
import com.example.taskplanner.data.model.entity.TypeNotes
import com.example.taskplanner.data.model.repository.OneNoteRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class PlannerCalendarViewModel @Inject constructor(private val repository: OneNoteRepository) :
    ViewModel() {

    suspend fun getDay(date: LocalDate): Day {
        return repository.getDay(date)
    }

    suspend fun deleteNote(note: TypeNotes) {
        repository.deleteNote(note)
    }

    suspend fun changeFinishNote(note: TypeNotes) {
        repository.changeFinishNote(note)
    }
}
