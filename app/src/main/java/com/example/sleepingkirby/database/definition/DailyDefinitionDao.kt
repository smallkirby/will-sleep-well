package com.example.sleepingkirby.database.definition

import androidx.room.*
import com.example.sleepingkirby.database.definition.event.DailyDefinition
import kotlinx.coroutines.flow.Flow

// DAO of DailyEvent entries
@Dao
interface DailyDefinitionDao {
    @Query("SELECT * FROM `daily-definitions_table`")
    fun getAllDailyDefinitions(): Flow<List<DailyDefinition>>

    @Query("SELECT * FROM `daily-definitions_table` WHERE id = :id")
    suspend fun get(id: Int): DailyDefinition?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: DailyDefinition)

    @Update
    suspend fun update(event: DailyDefinition): Int

    @Delete
    suspend fun delete(event: DailyDefinition)

    @Query("DELETE FROM `daily-definitions_table` WHERE id = :id")
    suspend fun deleteById(id: Int)
}
