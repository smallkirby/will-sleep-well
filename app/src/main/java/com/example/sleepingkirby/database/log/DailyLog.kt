package com.example.sleepingkirby.database.log

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.*

class LogDateTypeConverter {
    @TypeConverter
    fun toDate(f: Long) = Date(f)

    @TypeConverter
    fun fromDate(d: Date): Long = d.time
}

@Entity(tableName = "daily-log_table")
data class DailyLog(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "definition") val taskId: Int,
    @ColumnInfo(name = "time") val time: Date,
)