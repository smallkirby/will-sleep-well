package com.example.sleepingkirby.database.definition

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sleepingkirby.database.definition.event.DailyDefinition
import com.example.sleepingkirby.database.definition.event.DefinitionTypeConverter
import com.example.sleepingkirby.database.definition.event.EventIconConverter
import com.example.sleepingkirby.database.definition.event.TimeRangeConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi

@Database(entities = [DailyDefinition::class], version = 4, exportSchema = false)
@TypeConverters(TimeRangeConverter::class, EventIconConverter::class, DefinitionTypeConverter::class)
abstract class DailyDefinitionDatabase: RoomDatabase() {

    abstract fun dailyDefinitionDao(): DailyDefinitionDao

    companion object {
        private var INSTANCE: DailyDefinitionDatabase? = null

        @InternalCoroutinesApi
        fun getDatabase(context: Context, scope: CoroutineScope): DailyDefinitionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        DailyDefinitionDatabase::class.java,
                        "daily-definitions_database"
                )
                        .fallbackToDestructiveMigration() // allow destruction of old DB when version is updated
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
