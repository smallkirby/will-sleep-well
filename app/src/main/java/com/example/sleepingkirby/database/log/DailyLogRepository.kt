package com.example.sleepingkirby.database.log

import kotlinx.coroutines.flow.Flow
import java.util.*

class DailyLogRepository(private val dailyLogDao: DailyLogDao) {
    private val now
        get() = run {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.time
        }
    val todaysLogs: Flow<List<DailyLog>> = dailyLogDao.getLogsFrom(now)

    suspend fun insert(log: DailyLog) = dailyLogDao.insert(log)

    suspend fun deleteById(id: Int) = dailyLogDao.deleteById(id)

    suspend fun update(log: DailyLog) = dailyLogDao.update(log)
}