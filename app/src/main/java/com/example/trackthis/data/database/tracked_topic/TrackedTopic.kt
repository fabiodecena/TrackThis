package com.example.trackthis.data.database.tracked_topic

import androidx.annotation.StringRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



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
    val timeSpent: Int
)