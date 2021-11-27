package com.example.sleepingkirby.database.dailytask

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// DAO of DailyTask entries
@Dao
interface DailyTaskDao {
    @Query("SELECT * FROM `daily-task_table`")
    fun getAllDailyTasks(): Flow<List<DailyTask>>

    @Query("SELECT * FROM `daily-task_table` WHERE name = :name")
    suspend fun getFromName(name: String): List<DailyTask>

    @Query("SELECT * FROM `daily-task_table` WHERE id = :id")
    suspend fun get(id: Int): DailyTask?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: DailyTask)

    @Update
    suspend fun update(task: DailyTask): Int

    @Delete
    suspend fun delete(task: DailyTask)
}