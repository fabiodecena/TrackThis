package com.example.trackthis.data.database.tracked_topic

import androidx.annotation.StringRes
import androidx.compose.ui.input.key.type
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Entity(tableName = "tracked_topics")
data class TrackedTopic(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "topic_name")
    @StringRes val name: Int,
    @ColumnInfo(name = "daily_effort")
    val dailyEffort: Double,
    @ColumnInfo(name = "final_goal")
    val finalGoal: Int,
    @ColumnInfo(name = "starting_date")
    val startingDate: String,
    @ColumnInfo(name = "ending_date")
    val endingDate: String,
    @ColumnInfo(name = "time_spent")
    val timeSpent: Int, // Change to  LONG // TODO: check if this works
    @ColumnInfo(name = "index")
    val index: Int,
    @ColumnInfo(name = "daily_time_spent")
    val dailyTimeSpent: Map<String, Long> = emptyMap()
)

class DailyTimeSpentConverter {

    @TypeConverter
    fun fromDailyTimeSpent(dailyTimeSpent: Map<String, Long>): String {
        return Gson().toJson(dailyTimeSpent) // Using Gson for serialization
    }

    @TypeConverter
    fun toDailyTimeSpent(dailyTimeSpentString: String): Map<String, Long> {
        val type = object : TypeToken<Map<String, Long>>() {}.type
        return Gson().fromJson(dailyTimeSpentString, type) // Using Gson for deserialization
    }
}