package com.example.trackthis.data

import androidx.annotation.StringRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "tracked_topics")
data class TrackedTopic(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "topic_name")
    @StringRes val name: Int,
    @ColumnInfo(name = "final_goal")
    val finalGoal: Int,
    @ColumnInfo(name = "starting_date")
    val startingDate: String,
    @ColumnInfo(name = "ending_date")
    val endingDate: String
)