package com.example.trackthis

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackthis.data.database.tracked_topic.HistoryDatabase
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.data.database.tracked_topic.TrackedTopicDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HistoryDatabaseTest {

    private lateinit var database: HistoryDatabase
    private lateinit var trackedTopicDao: TrackedTopicDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, HistoryDatabase::class.java)
            .allowMainThreadQueries() // For testing only
            .build()
        trackedTopicDao = database.trackedTopicDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun database_creation_is_successful() {
        assertNotNull(database)
    }


    @Test
    fun dao_access_is_successful() {
        assertNotNull(trackedTopicDao)
    }

    @Test
    fun insert_and_query_tracked_topic() = runBlocking {
        val trackedTopic = TrackedTopic(
            id = 1,
            userId = "1f",
            name = 123,
            dailyEffort = 3.0,
            finalGoal = 70,
            startingDate = "11/01/2025",
            endingDate = "11/20/2025",
            totalTimeSpent = 10,
            weeklyTimeSpent = 0,
            dailyTimeSpent = mapOf("Monday" to 3, "Tuesday" to 4, "Friday" to 3 )
        )
        trackedTopicDao.insert(trackedTopic)

        val retrievedTopic = trackedTopicDao.getItemByName("1f", 123)
        assertNotNull(retrievedTopic)
    }
}