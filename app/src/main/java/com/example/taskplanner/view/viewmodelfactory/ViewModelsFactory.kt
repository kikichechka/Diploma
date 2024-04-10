package com.example.taskplanner.view.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taskplanner.presentation.AddNewNoteViewModel
import com.example.taskplanner.presentation.PlannerListDaysViewModel
import javax.inject.Inject

class ViewModelsFactory @Inject constructor(
    private val plannerListDaysViewModel: PlannerListDaysViewModel,
    private val addNewNoteViewModel: AddNewNoteViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlannerListDaysViewModel::class.java)) {
            return plannerListDaysViewModel as T
        }

        if (modelClass.isAssignableFrom(AddNewNoteViewModel::class.java)) {
            return addNewNoteViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}