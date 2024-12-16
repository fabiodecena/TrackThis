package com.example.trackthis

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.trackthis.data.TopicListRepository
import com.example.trackthis.data.database.tracked_topic.HistoryDatabase


class TrackApplication : Application() {

    val database: HistoryDatabase by lazy { HistoryDatabase.getDatabase(this) }
    val topicListRepository: TopicListRepository by lazy { TopicListRepository(this) }
}
