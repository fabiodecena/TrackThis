package com.example.trackthis.ui.insert_track

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.trackthis.TrackApplication
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.data.database.tracked_topic.TrackedTopicDao
import kotlinx.coroutines.flow.Flow


/**
 * ViewModel to validate and insert items in the Room database.
 */
class TrackEntryViewModel(private val trackedTopicDao: TrackedTopicDao) : ViewModel() {
    /**
     * Inserts an [TrackedTopic] in the Room database
     */
    suspend fun addNewItem(trackedTopic: TrackedTopic) {
        trackedTopicDao.insert(trackedTopic)
    }

    suspend fun deleteItem(trackedTopic: TrackedTopic) {
        trackedTopicDao.delete(trackedTopic)
    }

    fun retrieveAllItems(): Flow<List<TrackedTopic>> {
        return trackedTopicDao.getAllItems()
    }

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as TrackApplication
                TrackEntryViewModel(application.database.trackedTopicDao())
            }
        }
    }
}


