package com.example.taskplanner.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class ViewModelsFactory @Inject constructor(
    private val plannerListDaysViewModel: PlannerListDaysViewModel,
    private val addNewTaskViewModel: AddNewTaskViewModel,
    private val changeTaskViewModel: ChangeTaskViewModel,
    private val plannerCalendarViewModel: PlannerCalendarViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlannerListDaysViewModel::class.java)) {
            return plannerListDaysViewModel as T
        }
        if (modelClass.isAssignableFrom(AddNewTaskViewModel::class.java)) {
            return addNewTaskViewModel as T
        }
        if (modelClass.isAssignableFrom(ChangeTaskViewModel::class.java)) {
            return changeTaskViewModel as T
        }
        if (modelClass.isAssignableFrom(PlannerCalendarViewModel::class.java)) {
            return plannerCalendarViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}
