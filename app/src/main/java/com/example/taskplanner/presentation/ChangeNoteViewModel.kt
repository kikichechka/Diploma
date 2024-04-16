package com.example.taskplanner.presentation

import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import com.example.taskplanner.R
import com.example.taskplanner.data.AddNoteRepository
import com.example.taskplanner.data.ChangeNoteRepository
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.StateType
import com.example.taskplanner.data.model.entity.TypeNotes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class ChangeNoteViewModel @Inject constructor(
    private val repository: ChangeNoteRepository
) : ViewModel() {

    suspend fun saveNote(note: TypeNotes) {
        repository.changeNote(note)
    }
}
