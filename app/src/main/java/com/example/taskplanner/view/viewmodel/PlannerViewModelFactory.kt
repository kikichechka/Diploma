package com.example.taskplanner.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class PlannerViewModelFactory @Inject constructor(
    private val plannerViewModel: PlannerViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlannerViewModel::class.java)) {
            return plannerViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}