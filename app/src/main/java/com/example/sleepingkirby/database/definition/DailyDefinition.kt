package com.example.sleepingkirby.database.definition.event

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

enum class Importance {
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

data class TimeRange(val start: String, val end: String) {

    override fun toString() = "$start-$end"

    override fun equals(other: Any?): Boolean {
        if (other !is TimeRange) return false
        val start = timeStrToInt(this.start)
        val end = timeStrToInt(this.end)
        val startOther = timeStrToInt(other.start)
        val endOther = timeStrToInt(other.end)

        return start == startOther && end == endOther
    }

    fun isEmptyTimeRange() = this == emptyTimeRange()
    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + end.hashCode()
        return result
    }

    companion object {
        fun emptyTimeRange() = TimeRange("xx:xx", "xx:xx")

        fun timeStrToInt(str: String): Pair<Int, Int>? {
            val parts = str.split(":")
            if(parts.size != 2) return null
            return try {
                val hour = parts[0].toInt()
                val minute = parts[1].toInt()
                hour to minute
            } catch (nfe: NumberFormatException) {
                null
            }
        }

        fun fromString(strStart: String, strEnd: String): TimeRange {
            if (!isTimeValid(strStart) && !isTimeValid(strEnd))
                return emptyTimeRange()
            val start = strStart.run {
                if(isTimeValid(this))
                    this
                else
                    "00:00"
            }
            val end = strEnd.run {
                if(isTimeValid(this))
                    this
                else
                    "23:59"
            }
            return TimeRange(start, end) // XXX ensure start < end
        }

        fun fromString(str: String): TimeRange {
            val parts = str.split("-")
            if (parts.size != 2) return TimeRange("00:00", "23:59")
            return fromString(parts[0], parts[1])
        }

        private fun isTimeValid(stime: String): Boolean {
            val (hour, minute) = timeStrToInt(stime) ?: return false
            return (0..23).contains(hour) && (0..59).contains(minute)
        }
    }
}

class TimeRangeConverter {
    @TypeConverter
    fun toTimeRange(value: String) = TimeRange.fromString(value)

    @TypeConverter
    fun fromTimeRange(value: TimeRange) = value.toString()
}

class EventIconConverter {
    @TypeConverter
    fun toEventIcon(id: Int): EventIcon = EventIcon.fromId(id)

    @TypeConverter
    fun fromEventIcon(value: EventIcon) = value.id
}

enum class DefinitionType {
    TASK,
    EVENT,
}

class DefinitionTypeConverter {
    @TypeConverter
    fun toDefinitionType(type: Int): DefinitionType = DefinitionType.values()[type]

    @TypeConverter
    fun fromDefinitionType(value:  DefinitionType) = value.ordinal
}

interface DailyDefinitionInterface {
    fun toDailyDefinition(): DailyDefinition
}

data class DailyEvent (
    val id: Int,
    val name: String,
    val goodTimeRange: TimeRange,
    val badTimeRange: TimeRange,
    val importance: Importance,
    val icon: EventIcon,
): DailyDefinitionInterface {
    override fun toDailyDefinition(): DailyDefinition
        = DailyDefinition(id, DefinitionType.EVENT, name, "xx:xx", goodTimeRange, badTimeRange, importance, icon)
}

data class DailyTask(
    val id: Int,
    val name: String,
    val time: String,
    val importance: Importance,
): DailyDefinitionInterface {
    override fun toDailyDefinition(): DailyDefinition
        = DailyDefinition(id, DefinitionType.TASK, name, time, TimeRange.emptyTimeRange(), TimeRange.emptyTimeRange(), importance, EventIcon.OTHER)
}

@Entity(tableName = "daily-definitions_table")
data class DailyDefinition(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "definitionType") val definitionType: DefinitionType,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "goodTimeRange") val goodTimeRange: TimeRange,
    @ColumnInfo(name = "badTimeRange") val badTimeRange: TimeRange,
    @ColumnInfo(name = "importance") val importance: Importance,
    @ColumnInfo(name = "icon") val icon: EventIcon,
) {
    fun toDailyTask(): DailyTask? =
        if(!isTask()) null
        else DailyTask(id, name, time, importance)

    fun toDailyEvent(): DailyEvent? =
        if (!isEvent()) null
        else DailyEvent(id, name, goodTimeRange, badTimeRange, importance, icon)

    fun isTask() = definitionType == DefinitionType.TASK
    fun isEvent() = definitionType == DefinitionType.EVENT
}
