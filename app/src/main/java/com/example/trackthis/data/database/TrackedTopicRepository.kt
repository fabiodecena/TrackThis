package com.example.trackthis.data.database

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Item] from a given data source.
 */
interface TrackedTopicRepository {

    //Retrieve all the items from the the given data source.
    fun getAllItemsStream(): Flow<List<TrackedTopic>>

    //Retrieve an item from the given data source that matches with the [id].

    fun getItemStream(id: Int): Flow<TrackedTopic?>

    //Insert item in the data source
    suspend fun insertItem(trackedTopic: TrackedTopic)

    //Delete item from the data source
    suspend fun deleteItem(trackedTopic: TrackedTopic)

    //Update item in the data source
    suspend fun updateItem(trackedTopic: TrackedTopic)
}

