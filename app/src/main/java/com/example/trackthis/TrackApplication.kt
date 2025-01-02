package com.example.trackthis

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import com.example.trackthis.data.TopicListRepository
import com.example.trackthis.data.database.tracked_topic.HistoryDatabase
import com.google.firebase.FirebaseApp

/**
 * [TrackApplication] is the main application class for the Track_This app.
 *
 * It initializes the database, repositories, and registers a [BroadcastReceiver]
 * for time and timezone changes. It also initializes Firebase.
 */

class TrackApplication : Application() {
    /**
     * [TimeChangeReceiver] to listen for time and timezone changes.
     */
    private val timeChangeReceiver = TimeChangeReceiver()
    /**
     * The [HistoryDatabase] instance for the application used to store tracked topics data with Room.
     * The [TopicListRepository] instance for the application used to manage the list of topics with DataStore.
     * They are lazily initialized to ensure they are created only when needed
     * and are used for data persistence along the application's lifecycle.
     */
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
    /**
     * This method is called when the application is being terminated.
     *
     * It unregisters the [timeChangeReceiver] to prevent memory leaks.
     */
    override fun onTerminate() {
        super.onTerminate()
        unregisterReceiver(timeChangeReceiver)
    }
}
