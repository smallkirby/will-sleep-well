package com.example.sleepingkirby.database.log

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface DailyLogDao {
    @Query("SELECT * FROM `daily-log_table` WHERE time BETWEEN :start AND :end")
    fun getLogsBetween(start: Date, end: Date): Flow<List<DailyLog>>

    @Query("SELECT * FROM `daily-log_table` WHERE time >= :start")
    fun getLogsFrom(start: Date): Flow<List<DailyLog>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(log: DailyLog)

    @Update
    suspend fun update(log: DailyLog)

    @Delete
    suspend fun delete(log: DailyLog)

    @Query("DELETE FROM `daily-log_table` WHERE id = :id")
    suspend fun deleteById(id: Int)
}