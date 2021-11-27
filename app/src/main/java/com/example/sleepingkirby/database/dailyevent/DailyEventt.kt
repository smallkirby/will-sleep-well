package com.example.sleepingkirby.database.dailyevent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.sleepingkirby.database.dailytask.Importance

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

// DailyEvent holds defined daily events.
@Entity(tableName = "daily-event_table")
data class DailyEvent(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "goodTimeRange") val goodTimeRange: TimeRange,
        @ColumnInfo(name = "badTimeRange") val badTimeRange: TimeRange,
        @ColumnInfo(name = "importance") val importance: Importance,
        @ColumnInfo(name = "icon") val icon: EventIcon,
)
