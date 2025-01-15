package com.example.trackthis.insert_track

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackthis.data.database.tracked_topic.HistoryDatabase
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.data.database.tracked_topic.TrackedTopicDao
import com.example.trackthis.ui.insert_track.TrackEntryViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This class contains JUnit tests to verify the functionality of the [TrackEntryViewModel].
 * This Unit tests are located within the androidTest folder to maintain a clean overview.
 * It tests the validation of user inputs, specifically:
 * - Daily effort cannot be greater than the final goal.
 * - Daily effort cannot be greater than 24 hours.
 * - Starting date cannot be greater than the ending date.
 * It also tests the following database operations managed by the [TrackEntryViewModel]:
 * - Adding a new tracked topic.
 * - Retrieving all tracked topics by user ID.
 * - Deleting a tracked topic.
 */
@RunWith(AndroidJUnit4::class)
class TrackEntryViewModelTest {
    private lateinit var database: HistoryDatabase
    private lateinit var trackedTopicDao: TrackedTopicDao
    private lateinit var trackEntryViewModel: TrackEntryViewModel

    @Before
    fun setup() {
        // Create an in-memory database for testing
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, HistoryDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        trackedTopicDao = database.trackedTopicDao()
        trackEntryViewModel = TrackEntryViewModel(trackedTopicDao)
    }

    @After
    fun tearDown() {
        database.close()
    }
    @Test
    fun testValidateInputs_invalidDailyEffortGreaterThanFinalGoal() = runBlocking {
        trackEntryViewModel.updateDailyEffort("15")
        trackEntryViewModel.updateFinalGoal("10")
        trackEntryViewModel.updateStartingDate("01/15/2025")
        trackEntryViewModel.updateEndingDate("01/20/2025")

        val state = trackEntryViewModel.trackEntryUiState.first()

        Assert.assertTrue(state.isDailyEffortErrorGreaterThanFinalGoal)
        Assert.assertFalse(state.isDailyEffortErrorGreaterThan24)
        Assert.assertFalse(state.isDateError)
        Assert.assertFalse(state.isFormValid)
    }

    @Test
    fun testValidateInputs_invalidDailyEffortGreaterThan24() = runBlocking {
        trackEntryViewModel.updateDailyEffort("25")
        trackEntryViewModel.updateFinalGoal("50")
        trackEntryViewModel.updateStartingDate("01/15/2025")
        trackEntryViewModel.updateEndingDate("01/20/2025")

        val state = trackEntryViewModel.trackEntryUiState.first()

        Assert.assertFalse(state.isDailyEffortErrorGreaterThanFinalGoal)
        Assert.assertTrue(state.isDailyEffortErrorGreaterThan24)
        Assert.assertFalse(state.isDateError)
        Assert.assertFalse(state.isFormValid)
    }

    @Test
    fun testValidateInputs_invalidStartingDateGreaterThanEndingDate() = runBlocking {
        trackEntryViewModel.updateDailyEffort("5")
        trackEntryViewModel.updateFinalGoal("20")
        trackEntryViewModel.updateStartingDate("01/25/2025")
        trackEntryViewModel.updateEndingDate("01/20/2025")

        val state = trackEntryViewModel.trackEntryUiState.first()

        Assert.assertFalse(state.isDailyEffortErrorGreaterThanFinalGoal)
        Assert.assertFalse(state.isDailyEffortErrorGreaterThan24)
        Assert.assertTrue(state.isDateError)
        Assert.assertFalse(state.isFormValid)
    }

    @Test
    fun test_AddNewItem_RetrieveAllItemsByUserId_and_DeleteItem_functions() = runBlocking {
        val trackedTopic = TrackedTopic(
            id = 1,
            userId = "testUser",
            name = 2131492868,
            dailyEffort = 3.0,
            finalGoal = 70,
            startingDate = "01/15/2025",
            endingDate = "01/20/2025",
            totalTimeSpent = 10,
            weeklyTimeSpent = 0,
            dailyTimeSpent = mapOf("Monday" to 3)
        )
        trackEntryViewModel.userId = trackedTopic.userId
        trackEntryViewModel.addNewItem(trackedTopic)
        val items = trackEntryViewModel.retrieveAllItemsByUserId().first()
        Assert.assertEquals(1, items.size)
        Assert.assertEquals(trackedTopic, items[0])
        trackEntryViewModel.deleteItem(trackedTopic)
        val updatedItems = trackEntryViewModel.retrieveAllItemsByUserId().first()
        Assert.assertEquals(0, updatedItems.size)

    }
}