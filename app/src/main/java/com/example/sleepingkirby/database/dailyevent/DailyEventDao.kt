package com.example.sleepingkirby.database.dailyevent

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// DAO of DailyEvent entries
@Dao
interface DailyEventDao {
    @Query("SELECT * FROM `daily-event_table`")
    fun getAllDailyEvents(): Flow<List<DailyEvent>>

    @Query("SELECT * FROM `daily-event_table` WHERE id = :id")
    suspend fun get(id: Int): DailyEvent?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: DailyEvent)

    @Update
    suspend fun update(event: DailyEvent): Int

    @Delete
    suspend fun delete(event: DailyEvent)
}
