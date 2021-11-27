package com.example.sleepingkirby.database.dailyevent

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi

// DailyEventDatabase is a singleton instance which indeed interacts with DB via DAO.
@Database(entities = [DailyEvent::class], version = 3, exportSchema = false)
@TypeConverters(TimeRangeConverter::class, EventIconConverter::class)
abstract class DailyEventDatabase: RoomDatabase() {

    abstract fun dailyEventDao(): DailyEventDao

    companion object {
        private var INSTANCE: DailyEventDatabase? = null

        @InternalCoroutinesApi
        fun getDatabase(context: Context, scope: CoroutineScope): DailyEventDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        DailyEventDatabase::class.java,
                        "daily-event_database"
                )
                        .fallbackToDestructiveMigration() // allow destruction of old DB when version is updated
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
