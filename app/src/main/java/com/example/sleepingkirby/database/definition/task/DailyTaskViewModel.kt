package com.example.sleepingkirby.database.definition.event.dailytask

import androidx.lifecycle.*
import com.example.sleepingkirby.database.definition.event.DailyDefinition
import com.example.sleepingkirby.database.definition.DailyDefinitionRepository
import com.example.sleepingkirby.database.definition.event.DailyTask
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

// ViewModel which holds all defined DailyTask
class DailyTaskViewModel(private val repository: DailyDefinitionRepository): ViewModel() {
    private val allDailyDefinitions: LiveData<List<DailyDefinition>> = repository.allDailyDefinitions.asLiveData()
    val allDailyTasks: LiveData<List<DailyTask>> =
        Transformations.map(allDailyDefinitions) { it.filter { cand->
            cand.isTask()
        }.map { cand ->
            cand.toDailyTask()!!
        }}

    fun insert(task: DailyTask) = viewModelScope.launch {
        repository.insert(task)
    }

    fun get(id: Int, callback: (DailyTask?) -> Unit) = viewModelScope.launch {
        val task = repository.getTask(id)
        callback(task)
    }

    fun deleteById(id: Int) = viewModelScope.launch {
        repository.deleteById(id)
    }

    fun update(task: DailyTask) = viewModelScope.launch {
        repository.update(task)
    }
}

class DailyTaskViewModelFactory(private val repository: DailyDefinitionRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DailyTaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DailyTaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}