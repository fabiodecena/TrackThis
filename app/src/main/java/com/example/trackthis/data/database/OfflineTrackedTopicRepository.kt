package com.example.trackthis.data.database

import kotlinx.coroutines.flow.Flow

class OfflineTrackedTopicRepository(private val trackedTopicDao: TrackedTopicDao) :
    TrackedTopicRepository {
    override fun getAllItemsStream(): Flow<List<TrackedTopic>> = trackedTopicDao.getAllItems()

    override fun getItemStream(id: Int): Flow<TrackedTopic?> = trackedTopicDao.getItem(id)

    override suspend fun insertItem(trackedTopic: TrackedTopic) = trackedTopicDao.insert(trackedTopic)

    override suspend fun deleteItem(trackedTopic: TrackedTopic) = trackedTopicDao.delete(trackedTopic)

    override suspend fun updateItem(trackedTopic: TrackedTopic) = trackedTopicDao.update(trackedTopic)
}