package com.example.trackthis

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
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
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class MondayResetWorkerTest {
    private lateinit var database: HistoryDatabase
    private lateinit var trackedTopicDao: TrackedTopicDao
    private lateinit var worker: MondayResetWorker
    companion object {
        private lateinit var staticTrackedTopicDao: TrackedTopicDao
        private lateinit var trackedTopic1: TrackedTopic
        private lateinit var trackedTopic2: TrackedTopic
        private lateinit var trackedTopic3: TrackedTopic

        /**
         * Sets up the database and DAO before all tests.
         * It creates an in-memory database to avoid affecting the actual database.
         * It also inserts the tracked topics that will be used by the tests.
         */
        @BeforeClass
        @JvmStatic
        fun setupDatabaseAndInsertData() {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val database = Room.inMemoryDatabaseBuilder(context, HistoryDatabase::class.java)
                .allowMainThreadQueries()
                .build()
            staticTrackedTopicDao = database.trackedTopicDao()

            trackedTopic1 = TrackedTopic(
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
            trackedTopic2 = TrackedTopic(
                id = 2,
                userId = "1f",
                name = 456,
                dailyEffort = 2.5,
                finalGoal = 50,
                startingDate = "12/01/2025",
                endingDate = "12/20/2025",
                totalTimeSpent = 15,
                weeklyTimeSpent = 5,
                dailyTimeSpent = mapOf("Wednesday" to 2, "Thursday" to 3)
            )
            trackedTopic3 = TrackedTopic(
                id = 3,
                userId = "2g",
                name = 789,
                dailyEffort = 4.0,
                finalGoal = 100,
                startingDate = "01/01/2026",
                endingDate = "01/30/2026",
                totalTimeSpent = 20,
                weeklyTimeSpent = 10,
                dailyTimeSpent = mapOf("Monday" to 4, "Friday" to 6)
            )
            runBlocking {
                staticTrackedTopicDao.insert(trackedTopic1)
                staticTrackedTopicDao.insert(trackedTopic2)
                staticTrackedTopicDao.insert(trackedTopic3)
            }
        }
    }
    /**
     * Sets up the DAO before each test.
     */
    @Before
    fun setup() {
        val applicationContext = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(applicationContext, HistoryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        trackedTopicDao = database.trackedTopicDao()

        val context = InstrumentationRegistry.getTargetContext()
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(androidx.work.testing.SynchronousExecutor())
            .build()

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }

    @After
    fun teardown() {
        database.close()

    }

    @Test
    @Throws(Exception::class)
    fun doWork_when_today_is_Monday() = runBlocking {
        trackedTopicDao.insert(trackedTopic1)
        val trackedTopic = trackedTopicDao.getAllItems().first().first()
        val localDate = LocalDate.of(2025, 1, 5) // Example: Set the date to a Monday
        val currentDayOfWeek = localDate.dayOfWeek
        // Create request
        val request = OneTimeWorkRequestBuilder<MondayResetWorker>()
            .build()

        val workManager = WorkManager.getInstance(ApplicationProvider.getApplicationContext())
        // Enqueue and wait for result. This also runs the Worker synchronously
        // because we are using a SynchronousExecutor.
        workManager.enqueue(request).result.get()

        // Get WorkInfo and outputData
        val workInfo = workManager.getWorkInfoById(request.id).get()
        val outputData = workInfo.outputData
        val status = outputData.getString("status")
        if (currentDayOfWeek == DayOfWeek.MONDAY) {
            assertThat(workInfo.state, `is`(WorkInfo.State.SUCCEEDED))
            assertThat(status, `is`("done"))
            assertThat(trackedTopic.weeklyTimeSpent, `is`(trackedTopic.totalTimeSpent))
            assertThat(trackedTopic.dailyTimeSpent.values.sum(), `is`(0))
        }else {
            assertThat(workInfo.state, `is`(WorkInfo.State.RUNNING))
            assert(status == null)
            assertThat(trackedTopic.weeklyTimeSpent, `is`(0))
            assertThat(trackedTopic.dailyTimeSpent.values.sum().toInt(), `is`(trackedTopic.totalTimeSpent))
        }
    }
}