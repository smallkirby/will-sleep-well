package com.example.sleepingkirby.database.dailyevent

import kotlinx.coroutines.flow.Flow

// Abstraction layer between data source (eg: RoomDB) and UI for DailyEvent data
// direct access with Room is via DAO
class DailyEventRepository(private val dailyEventDao: DailyEventDao) {
    val allDailyEvents: Flow<List<DailyEvent>> = dailyEventDao.getAllDailyEvents()

    suspend fun insert(event: DailyEvent) {
        dailyEventDao.insert(event)
    }

    suspend fun get(id: Int) = dailyEventDao.get(id)

    suspend fun update(event: DailyEvent) = dailyEventDao.update(event)
}
