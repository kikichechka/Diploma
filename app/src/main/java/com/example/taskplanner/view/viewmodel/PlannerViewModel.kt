package com.example.taskplanner.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskplanner.domain.MyCalendar
import com.example.taskplanner.model.Repository
import com.example.taskplanner.model.RepositoryImpl

class PlannerViewModel(private val liveData: MutableLiveData<MyCalendar> = MutableLiveData<MyCalendar>(),
                       private val repository: Repository = RepositoryImpl()
): ViewModel() {

    fun getLiveData():MutableLiveData<MyCalendar>{
        return liveData
    }

    fun sentRequest() {
        liveData.value = repository.getCalendar()
    }
}
