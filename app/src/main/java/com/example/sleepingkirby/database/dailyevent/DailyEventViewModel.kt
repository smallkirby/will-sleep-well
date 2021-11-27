package com.example.sleepingkirby.database.dailyevent

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class DailyEventViewModel(private val repository: DailyEventRepository): ViewModel() {
    val allDailyEvents: LiveData<List<DailyEvent>> = repository.allDailyEvents.asLiveData()

    fun insert(event: DailyEvent) = viewModelScope.launch {
        repository.insert(event)
    }

    fun get(id: Int, callback: (DailyEvent?) -> Unit) = viewModelScope.launch {
        val event = repository.get(id)
        callback(event)
    }

    fun update(event: DailyEvent) = viewModelScope.launch {
        repository.update(event)
    }
}

class DailyEventViewModelFactory(private val repository: DailyEventRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DailyEventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DailyEventViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
