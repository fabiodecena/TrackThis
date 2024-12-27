package com.example.trackthis

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import com.example.trackthis.data.TimeChangeReceiver
import com.example.trackthis.data.TopicListRepository
import com.example.trackthis.data.database.tracked_topic.HistoryDatabase
import com.google.firebase.FirebaseApp
import java.util.Locale


class TrackApplication : Application() {
    private val timeChangeReceiver = TimeChangeReceiver()
    val database: HistoryDatabase by lazy { HistoryDatabase.getDatabase(this) }
    val topicListRepository: TopicListRepository by lazy { TopicListRepository(this) }

    override fun onCreate() {
        super.onCreate()

        // Register BroadcastReceiver for time and timezone changes
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
        }
        registerReceiver(timeChangeReceiver, filter)


        // Initialize Firebase
        FirebaseApp.initializeApp(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterReceiver(timeChangeReceiver)
    }
}
