package com.example.sleepingkirby.database.definition.event

import androidx.lifecycle.*
import com.example.sleepingkirby.database.definition.DailyDefinitionRepository
import kotlinx.coroutines.launch

class DailyEventViewModel(private val repository: DailyDefinitionRepository): ViewModel() {
    private val allDailyDefinitions: LiveData<List<DailyDefinition>> = repository.allDailyDefinitions.asLiveData()
    val allDailyEvents: LiveData<List<DailyEvent>> =
        Transformations.map(allDailyDefinitions) { it.filter { cand ->
            cand.isEvent()
        }.map { cand ->
            cand.toDailyEvent()!!
        } }


    fun insert(event: DailyEvent) = viewModelScope.launch {
        repository.insert(event)
    }

    fun get(id: Int, callback: (DailyEvent?) -> Unit) = viewModelScope.launch {
        val event = repository.getEvent(id)
        callback(event)
    }

    fun update(event: DailyDefinitionInterface) = viewModelScope.launch {
        repository.update(event)
    }
}

class DailyEventViewModelFactory(private val repository: DailyDefinitionRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DailyEventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DailyEventViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
