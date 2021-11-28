package com.example.sleepingkirby.database.log

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.util.*

class DailyLogViewModel(private val repository: DailyLogRepository): ViewModel() {
    val todaysLogs: LiveData<List<DailyLog>> = repository.todaysLogs.asLiveData()

    fun insert(log: DailyLog) = viewModelScope.launch {
        repository.insert(log)
    }

    fun insert(taskId: Int) = viewModelScope.launch {
        val log = DailyLog(0, taskId, Calendar.getInstance().time)
        repository.insert(log)
    }

    fun update(log: DailyLog) = viewModelScope.launch {
        repository.update(log)
    }

    fun deleteById(id: Int) = viewModelScope.launch {
        repository.deleteById(id)
    }
}

class DailyLogViewModelFactory(private val repository: DailyLogRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DailyLogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DailyLogViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
