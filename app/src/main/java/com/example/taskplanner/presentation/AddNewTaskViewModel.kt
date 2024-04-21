package com.example.taskplanner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskplanner.data.model.repository.AddTaskRepository
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.StateType
import com.example.taskplanner.data.model.entity.TypeTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddNewTaskViewModel @Inject constructor(
    private val repository: AddTaskRepository
) : ViewModel() {

    private val _stateTypeNote = MutableStateFlow(StateType.NOTE)
    val stateTypeNote = _stateTypeNote.asStateFlow()

    val listTypesNote = List(StateType.entries.size) {
        StateType.entries[it].value
    }

    suspend fun saveTask(task: TypeTask) {
        when (task) {
            is Note -> {
                repository.addTask(task)
            }

            is Reminder -> {
                repository.addTask(task)
            }

            is Medications -> {
                repository.addTask(task)
            }

            else -> {}
        }
    }

    suspend fun saveProducts(products: Products, list: List<String>) {
        viewModelScope.launch {
            repository.addProducts(products, list)
        }
    }

    fun changeStateTypeTask(position: StateType) {
        when (position) {
            StateType.NOTE -> _stateTypeNote.value = StateType.NOTE
            StateType.REMINDER -> _stateTypeNote.value = StateType.REMINDER
            StateType.PRODUCTS -> _stateTypeNote.value = StateType.PRODUCTS
            StateType.MEDICATIONS -> _stateTypeNote.value = StateType.MEDICATIONS
        }
    }
}
