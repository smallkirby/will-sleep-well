package com.example.sleepingkirby.database.dailytask

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.launch

// DailyTaskDatabase is a singleton instance which indeed interacts with DB via DAO.
@Database(entities = [DailyTask::class], version = 6, exportSchema = false)
@TypeConverters(ImportanceConverter::class)
abstract class DailyTaskDatabase: RoomDatabase() {

    abstract fun dailyTaskDao(): DailyTaskDao

    companion object {
        private var INSTANCE: DailyTaskDatabase? = null

        // get DB instance. the instance is singleton and has only one instance anytime.
        // it initiates instance in `scope`, which must have longer lifetime, such as ApplicationScope.
        @InternalCoroutinesApi
        fun getDatabase(context: Context, scope: CoroutineScope): DailyTaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DailyTaskDatabase::class.java,
                    "daily-task_database"
                )
                        .fallbackToDestructiveMigration() // allow destruction of old DB when version is updated
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }
}