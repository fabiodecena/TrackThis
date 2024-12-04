package com.example.trackthis.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackedTopicDao {

    @Query("SELECT * from tracked_topics ORDER BY id DESC")
    fun getAllItems(): Flow<List<TrackedTopic>>

    @Query("SELECT * from tracked_topics WHERE id = :id")
    fun getItem(id: Int): Flow<TrackedTopic>

    @Query("SELECT * from tracked_topics WHERE topic_name = :nameId")
    fun getItemByName(nameId: Int): Flow<TrackedTopic>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trackedTopic: TrackedTopic)

    @Update
    suspend fun update(trackedTopic: TrackedTopic)

    @Delete
    suspend fun delete(trackedTopic: TrackedTopic)
}