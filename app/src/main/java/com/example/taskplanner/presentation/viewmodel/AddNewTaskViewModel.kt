package com.example.taskplanner.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.StateType
import com.example.taskplanner.data.model.entity.TypeTask
import com.example.taskplanner.domain.AddProductsWithListUseCase
import com.example.taskplanner.domain.AddTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AddNewTaskViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val addProductsWithListUseCase: AddProductsWithListUseCase
) : ViewModel() {

    private val _stateTypeNote = MutableStateFlow(StateType.NOTE)
    val stateTypeNote = _stateTypeNote.asStateFlow()

    val listTypesNote = List(StateType.entries.size) {
        StateType.entries[it].value
    }

    suspend fun saveTask(task: TypeTask) {
        addTaskUseCase.addTask(task)
    }

    suspend fun saveProductsWithList(products: Products, list: List<String>) {
            addProductsWithListUseCase.addProductsWithList(products, list)
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
