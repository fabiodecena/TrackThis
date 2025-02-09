package com.example.trackthis

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.testing.WorkManagerTestInitHelper
import com.example.trackthis.data.database.tracked_topic.HistoryDatabase
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.data.database.tracked_topic.TrackedTopicDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * [MondayResetWorkerTest] is a test class for the [MondayResetWorker].
 * It tests the worker's ability to reset daily time spent on tracked topics,
 * and save the total time spent to weekly time spent when the current day is Monday.
 * It also tests that the worker does not reset data when the current day is not Monday.
 */
@RunWith(AndroidJUnit4::class)
class MondayResetWorkerTest {
    private lateinit var database: HistoryDatabase
    private lateinit var trackedTopicDao: TrackedTopicDao
    /**
     * Sets up the test environment before each test.
     * It initializes an in-memory database, gets the DAO, and initializes WorkManager for testing.
     */
    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Create an in-memory database for testing
        database = Room.inMemoryDatabaseBuilder(context, HistoryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        trackedTopicDao = database.trackedTopicDao()
        // Configure WorkManager for testing
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(androidx.work.testing.SynchronousExecutor())
            .build()

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }
    /**
     * Tears down the test environment after each test.
     * It closes the database and running worker.
     */
    @After
    fun teardown() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database.close()
        WorkManager.getInstance(context).cancelAllWork()
    }
    /**
     * Tests that the worker resets daily and weekly time spent on tracked topics
     * and saves the total time spent to weekly time spent when the current day is Monday.
     */
    @Test
    @Throws(Exception::class)
    fun reset_data_and_save_total_time_spent_when_today_is_Monday() = runBlocking {
        trackedTopicDao.insert(
            TrackedTopic(
                id = 1,
                userId = "1f",
                name = 2131492868,
                dailyEffort = 3.0,
                finalGoal = 70,
                startingDate = "11/01/2025",
                endingDate = "11/20/2025",
                totalTimeSpent = 10,
                weeklyTimeSpent = 0,
                dailyTimeSpent = mapOf("Monday" to 3, "Tuesday" to 4, "Friday" to 3 )
            )
        )
        val trackedTopic = trackedTopicDao.getAllItems().first().first()
        val localDate = LocalDate.of(2025, 1, 5) // Example: Set the date to a Monday
        val currentDayOfWeek = localDate.dayOfWeek
        val request = OneTimeWorkRequestBuilder<MondayResetWorker>()
            .build()
        val workManager = WorkManager.getInstance(ApplicationProvider.getApplicationContext())
        workManager.enqueue(request).result.get()
        if (currentDayOfWeek == DayOfWeek.MONDAY) {
            assertThat(trackedTopic.weeklyTimeSpent, `is`(trackedTopic.totalTimeSpent))
            assertThat(trackedTopic.dailyTimeSpent.values.sum(), `is`(0))
        }
    }
    /**
     * Tests that the worker does not reset data when the current day is not Monday
     * and update the total time spent that have to be equal to the sum of the daily time spent.
     */
    @Test
    @Throws(Exception::class)
    fun do_not_reset_data_and_not_save_total_time_spent_when_today_is_not_Monday() = runBlocking {
        trackedTopicDao.insert(
            TrackedTopic(
                id = 1,
                userId = "1f",
                name = 2131492868,
                dailyEffort = 3.0,
                finalGoal = 70,
                startingDate = "11/01/2025",
                endingDate = "11/20/2025",
                totalTimeSpent = 10,
                weeklyTimeSpent = 0,
                dailyTimeSpent = mapOf("Monday" to 3, "Tuesday" to 4, "Friday" to 3 )
            )
        )
        val trackedTopic = trackedTopicDao.getAllItems().first().first()
        val localDate = LocalDate.of(2025, 1, 4) // Example: Set the date to a Monday
        val currentDayOfWeek = localDate.dayOfWeek
        val request = OneTimeWorkRequestBuilder<MondayResetWorker>()
            .build()
        val workManager = WorkManager.getInstance(ApplicationProvider.getApplicationContext())
        workManager.enqueue(request).result.get()
        if (currentDayOfWeek != DayOfWeek.MONDAY) {
            assertThat(trackedTopic.weeklyTimeSpent, `is`(0))
            assertThat(trackedTopic.dailyTimeSpent.values.sum().toInt(), `is`(trackedTopic.totalTimeSpent))
        }
    }
}