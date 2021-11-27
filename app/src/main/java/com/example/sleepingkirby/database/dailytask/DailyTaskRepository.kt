package com.example.sleepingkirby.database.dailytask

import kotlinx.coroutines.flow.Flow

// Abstraction layer between data source (eg: RoomDB) and UI for DailyTask data
// direct access with Room is via DAO
class DailyTaskRepository(private val dailyTaskDao: DailyTaskDao) {
    val allDailyTasks: Flow<List<DailyTask>> = dailyTaskDao.getAllDailyTasks()

    suspend fun insert(task: DailyTask) {
        dailyTaskDao.insert(task)
    }

    private suspend fun getFromName(name: String) = dailyTaskDao.getFromName(name)

    suspend fun get(id: Int) = dailyTaskDao.get(id)

    suspend fun update(task: DailyTask) = dailyTaskDao.update(task)

    suspend fun removeFromName(name: String) {
        val tasks = getFromName(name)
        tasks.forEach {
            dailyTaskDao.delete(it)
        }
    }
}