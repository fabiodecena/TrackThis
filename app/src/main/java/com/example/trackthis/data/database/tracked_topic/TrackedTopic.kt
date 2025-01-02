package com.example.trackthis.data.database.tracked_topic

import androidx.annotation.StringRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.trackthis.ui.statistics.StatisticsScreen
/**
 * [TrackedTopic] is an entity that represents a tracked topic object in the database.
 *
 * It stores information about a user's learning or tracking goals, including the topic name,
 * daily effort, final goal, start and end dates, total time spent, and daily time spent.
 *
 * @property id The unique identifier for the tracked topic, autogenerated by the database.
 * @property userId The Firebase UID of the user tracking this topic.
 * @property name The string resource ID for the name of the topic.
 * @property dailyEffort The daily effort the user intends to dedicate to this topic.
 * @property finalGoal The final goal the user wants to achieve for this topic expressed in hours.
 * @property startingDate The date when the user started tracking this topic, in string format.
 * @property endingDate The date when the user plans to finish tracking this topic, in string format.
 * @property totalTimeSpent The total time spent on this topic, in hours.
 * @property weeklyTimeSpent The time spent on this topic in the current week, in hours.
 * @property dailyTimeSpent A map of days to time spent on this topic, in hours.
 */
@Entity(tableName = "tracked_topics")
data class TrackedTopic(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "user_id")
    val userId: String, // Firebase UID
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
    val totalTimeSpent: Int,
    @ColumnInfo(name = "index")
    val weeklyTimeSpent: Int,
    @ColumnInfo(name = "daily_time_spent")
    val dailyTimeSpent: Map<String, Long> = emptyMap()
)
/**
 * [DailyTimeSpentConverter] is a [TypeConverter] that handles the conversion of a
 * `Map<String, Long>` representing daily time spent to and from a string for database storage.
 * It is used to store the `dailyTimeSpent` map in the [TrackedTopic] entity.
 * `dailyTimeSpent` is used to visualize user progress in the [StatisticsScreen]
 * It uses Gson for serialization and deserialization.
 */
class DailyTimeSpentConverter {
    /**
     * Converts a [Map] of dates to time spent to a JSON string.
     *
     * @param dailyTimeSpent The map of dates to time spent.
     * @return A JSON string representation of the map.
     */
    @TypeConverter
    fun fromDailyTimeSpent(dailyTimeSpent: Map<String, Long>): String {
        return Gson().toJson(dailyTimeSpent) // Using Gson for serialization
    }
    /**
     * Converts a JSON string back to a [Map] of dates to time spent.
     *
     * @param dailyTimeSpentString The JSON string representation of the map.
     * @return A map of dates to time spent.
     */
    @TypeConverter
    fun toDailyTimeSpent(dailyTimeSpentString: String): Map<String, Long> {
        val type = object : TypeToken<Map<String, Long>>() {}.type
        return Gson().fromJson(dailyTimeSpentString, type) // Using Gson for deserialization
    }
}
