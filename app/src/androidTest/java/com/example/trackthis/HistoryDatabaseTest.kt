package com.example.trackthis

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackthis.data.database.tracked_topic.HistoryDatabase
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.data.database.tracked_topic.TrackedTopicDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * [HistoryDatabaseTest] is a test class for the [HistoryDatabase].
 * It provides unit tests for the database and its DAO.
 */
@RunWith(AndroidJUnit4::class)
class HistoryDatabaseTest {

    private lateinit var database: HistoryDatabase
    private lateinit var trackedTopicDao: TrackedTopicDao

    /**
     * Sets up the database and DAO before each test.
     * It creates an in-memory database to avoid affecting the actual database.
     */
    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, HistoryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        trackedTopicDao = database.trackedTopicDao()
    }

    /**
     * Closes the database after each test.
     */
    @After
    fun teardown() {
        database.close()
    }

    /**
     * Tests if the database creation is successful.
     */
    @Test
    fun database_creation_is_successful() {
        assertNotNull(database)
    }

    /**
     * Tests if the DAO access is successful.
     */
    @Test
    fun dao_access_is_successful() {
        assertNotNull(trackedTopicDao)
    }

    /**
     * Tests inserting a tracked topic and querying it by name.
     */
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

    /**
     *  Test case for inserting and querying all tracked topics.
     *  They are correctly ordered from the most recent to the oldest.
     *  They are also deleted and reordered correctly.
     */
    @Test
    fun insert_and_query_all_tracked_topics_and_check_order_delete_the_middle_item_and_check_order_again() = runBlocking {
        val trackedTopic1 = TrackedTopic(
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
        val trackedTopic2 = TrackedTopic(
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
        val trackedTopic3 = TrackedTopic(
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
        trackedTopicDao.insert(trackedTopic1)
        trackedTopicDao.insert(trackedTopic2)
        trackedTopicDao.insert(trackedTopic3)

        val allTopics = trackedTopicDao.getAllItems().first()
        Assert.assertEquals(3, allTopics.size)
        Assert.assertEquals(trackedTopic1, allTopics[2])
        Assert.assertEquals(trackedTopic2, allTopics[1])
        Assert.assertEquals(trackedTopic3, allTopics[0])

        trackedTopicDao.delete(trackedTopic2)
        val allNewTopics = trackedTopicDao.getAllItems().first()
        Assert.assertEquals(2, allNewTopics.size)
        Assert.assertEquals(trackedTopic1, allNewTopics[1])
        Assert.assertEquals(trackedTopic3, allNewTopics[0])
    }
}