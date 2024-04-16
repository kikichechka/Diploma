package com.example.taskplanner.presentation

import androidx.lifecycle.ViewModel
import com.example.taskplanner.data.AddNoteRepository
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.StateType
import com.example.taskplanner.data.model.entity.TypeNotes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AddNewNoteViewModel @Inject constructor(
    private val repository: AddNoteRepository
) : ViewModel() {

    private val _stateTypeNote = MutableStateFlow(StateType.NOTE)
    val stateTypeNote = _stateTypeNote.asStateFlow()

    val listTypesNote = List(StateType.entries.size) {
        StateType.entries[it].value
    }

    suspend fun saveNote(note: TypeNotes) {
        when(note) {
            is Note -> {
                repository.addNote(note)
            }
            is Reminder -> {
                repository.addReminder(note)
            }

            is Products -> {
                repository.addProducts(note)
            }

            is Medications -> {
                repository.addMedications(note)
            }
        }
    }

    fun changeStateTypeNote(position: StateType) {
        when(position) {
            StateType.NOTE -> _stateTypeNote.value = StateType.NOTE
            StateType.REMINDER -> _stateTypeNote.value = StateType.REMINDER
            StateType.PRODUCTS -> _stateTypeNote.value = StateType.PRODUCTS
            StateType.MEDICATIONS -> _stateTypeNote.value = StateType.MEDICATIONS
        }
    }
}
