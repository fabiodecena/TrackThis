package com.example.trackthis

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.trackthis.data.TimeChangeReceiver
import com.example.trackthis.data.TopicListRepository
import com.example.trackthis.data.database.tracked_topic.HistoryDatabase


class TrackApplication : Application() {
    private val timeChangeReceiver = TimeChangeReceiver()

    override fun onCreate() {
        super.onCreate()

        // Register BroadcastReceiver for time and timezone changes
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
        }
        registerReceiver(timeChangeReceiver, filter)
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterReceiver(timeChangeReceiver)
    }
    val database: HistoryDatabase by lazy { HistoryDatabase.getDatabase(this) }
    val topicListRepository: TopicListRepository by lazy { TopicListRepository(this) }
}
