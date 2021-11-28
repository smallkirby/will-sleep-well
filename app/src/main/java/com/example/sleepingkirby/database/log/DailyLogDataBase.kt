package com.example.sleepingkirby.database.log

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi

@Database(entities = [DailyLog::class], version = 1, exportSchema = false)
@TypeConverters(LogDateTypeConverter::class)
abstract class DailyLogDataBase: RoomDatabase() {

    abstract fun dailyLogDao(): DailyLogDao

    companion object {
        private var INSTANCE: DailyLogDataBase? = null

        @InternalCoroutinesApi
        fun getDatabase(context: Context, scope: CoroutineScope): DailyLogDataBase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DailyLogDataBase::class.java,
                    "daily-log_database"
                )
                    .fallbackToDestructiveMigration() // allow destruction of old DB when version is updated
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}