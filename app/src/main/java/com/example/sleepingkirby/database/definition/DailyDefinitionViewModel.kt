package com.example.sleepingkirby.database.definition

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.sleepingkirby.database.definition.event.DailyDefinition

class DailyDefinitionViewModel(private val repository: DailyDefinitionRepository): ViewModel() {
    val allDailyDefinitions: LiveData<List<DailyDefinition>> = repository.allDailyDefinitions.asLiveData()
}

class DailyDefinitionViewModelFactory(private val repository: DailyDefinitionRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DailyDefinitionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DailyDefinitionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
