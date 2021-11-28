package com.example.sleepingkirby.database.definition

import com.example.sleepingkirby.database.definition.event.DailyDefinition
import com.example.sleepingkirby.database.definition.event.DailyDefinitionInterface
import com.example.sleepingkirby.database.definition.event.DailyEvent
import com.example.sleepingkirby.database.definition.event.DailyTask
import kotlinx.coroutines.flow.Flow

class DailyDefinitionRepository(private val dailyDefinitionDao: DailyDefinitionDao) {
    val allDailyDefinitions: Flow<List<DailyDefinition>> = dailyDefinitionDao.getAllDailyDefinitions()

    suspend fun insert(inter: DailyDefinitionInterface) {
        val definition = inter.toDailyDefinition()
        dailyDefinitionDao.insert(definition)
    }

    private suspend fun get(id: Int) = dailyDefinitionDao.get(id)

    suspend fun getEvent(id: Int): DailyEvent? {
        val item = get(id) ?: return null
        return if(item.isEvent()) item.toDailyEvent() else null
    }

    suspend fun getTask(id: Int): DailyTask? {
        val item = get(id) ?: return null
        return if(item.isTask()) item.toDailyTask() else null
    }

    suspend fun deleteById(id: Int) = dailyDefinitionDao.deleteById(id)

    suspend fun update(inter: DailyDefinitionInterface) = dailyDefinitionDao.update(inter.toDailyDefinition())
}
