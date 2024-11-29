package com.example.trackthis.data.database

import android.content.Context

//App container for Dependency injection.
interface AppContainer {
    val trackedTopicRepository: TrackedTopicRepository
}
//[AppContainer] implementation that provides instance of [OfflineTrackedTopicRepository]
class AppDataContainer(private val context: Context) : AppContainer {
    //Implementation for [TrackedTopicRepository]
    override val trackedTopicRepository: TrackedTopicRepository by lazy {
        OfflineTrackedTopicRepository(HistoryDatabase.getDatabase(context).itemDao())
    }
}
