package com.example.sleepingkirby.database.dailytask

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

enum class Importance() {
    INFO,
    LOW,
    MED,
    HIGH,
    CRITICAL,
}

class ImportanceConverter {
    @TypeConverter
    fun toImportance(value: String) = enumValueOf<Importance>(value)

    @TypeConverter
    fun fromImportance(value: Importance) = value.name
}

// DailyTask holds defined daily task.
@Entity(tableName = "daily-task_table")
data class DailyTask(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "importance") val importance: Importance
) {
}