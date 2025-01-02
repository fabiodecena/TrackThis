package com.example.trackthis.data.database.tracked_topic

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * [TrackedTopicDao] is an interface that defines database operations for the [TrackedTopic] entity.
 *
 * This DAO provides methods for retrieving, inserting, updating, and deleting [TrackedTopic]
 * entities from the database. It uses Room annotations to define the database interactions.
 */
@Dao
interface TrackedTopicDao {
    /**
     * Retrieves all tracked topics from the database, ordered by ID in descending order.
     *
     * @return A [Flow] emitting a list of all [TrackedTopic] entities in the database.
     */
    @Query("SELECT * from tracked_topics ORDER BY id DESC")
    fun getAllItems(): Flow<List<TrackedTopic>>
    /**
     * Retrieves all tracked topics for a specific user from the database, ordered by ID in descending order.
     *
     * @param userId The ID of the user whose tracked topics are to be retrieved.
     * @return A [Flow] emitting a list of [TrackedTopic] entities associated with the given user ID.
     */
    @Query("SELECT * from tracked_topics WHERE user_id = :userId ORDER BY id DESC")
    fun getAllItemsByUser(userId: String): Flow<List<TrackedTopic>>
    /**
     * Retrieves a specific tracked topic by user ID and topic name.
     *
     * @param userId The ID of the user.
     * @param nameId The name of the topic to retrieve.
     * @return A [Flow] emitting the [TrackedTopic] entity matching the given user ID and topic name,
     * or null if no such topic exists.
     */
    @Query("SELECT * from tracked_topics WHERE user_id = :userId AND topic_name = :nameId")
    fun getItemByName(userId: String, nameId: Int): Flow<TrackedTopic>
    /**
     * Inserts a new tracked topic into the database.
     *
     * If a topic with the same primary key already exists, the insertion is ignored due to the
     * [OnConflictStrategy.IGNORE] conflict strategy.
     *
     * @param trackedTopic The [TrackedTopic] entity to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trackedTopic: TrackedTopic)
    /**
     * Updates an existing tracked topic in the database.
     *
     * @param trackedTopic The [TrackedTopic] entity to be updated.
     */
    @Update
    suspend fun update(trackedTopic: TrackedTopic)
    /**
     * Deletes a tracked topic from the database.
     *
     * @param trackedTopic The [TrackedTopic] entity to be deleted.
     */
    @Delete
    suspend fun delete(trackedTopic: TrackedTopic)
}