package com.example.sleepingkirby.database.dailytask

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

// ViewModel which holds all defined DailyTask
class DailyTaskViewModel(private val repository: DailyTaskRepository): ViewModel() {
    val allDailyTasks: LiveData<List<DailyTask>> = repository.allDailyTasks.asLiveData()

    fun insert(task: DailyTask) = viewModelScope.launch {
        repository.insert(task)
    }

    fun removeFromName(name: String) = viewModelScope.launch {
        repository.removeFromName(name)
    }

    fun get(id: Int, callback: (DailyTask?) -> Unit) = viewModelScope.launch {
        val task = repository.get(id)
        callback(task)
    }

    fun update(task: DailyTask) = viewModelScope.launch {
        repository.update(task)
    }
}

class DailyTaskViewModelFactory(private val repository: DailyTaskRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DailyTaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DailyTaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}