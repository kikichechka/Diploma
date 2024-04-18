package com.example.taskplanner.presentation

import androidx.lifecycle.ViewModel
import com.example.taskplanner.data.model.repository.ChangeNoteRepository
import com.example.taskplanner.data.model.entity.TypeNotes
import javax.inject.Inject

class ChangeNoteViewModel @Inject constructor(
    private val repository: ChangeNoteRepository
) : ViewModel() {

    suspend fun saveNote(note: TypeNotes) {
        repository.changeNote(note)
    }
}
