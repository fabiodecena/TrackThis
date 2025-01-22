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
 * These tests verify the behavior of the TrackEntryViewModel.
 *
 * The tests cover the following scenarios:
 * - Validating user inputs for daily effort, final goal, and start/end dates.
 * - Adding a new tracked topic to the database.
 * - Retrieving all tracked topics for a specific user.
 * - Deleting a tracked topic from the database.
 *
 * The tests use an in-memory database to ensure that they are isolated and do not affect the
 * actual application data.
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

    /**
     * Tests that the ViewModel correctly identifies an invalid daily effort that is greater than the final goal.
     */
    @Test
    fun test_Validate_Inputs_invalid_DailyEffortGreaterThanFinalGoal() = runBlocking {
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
    /**
     * Tests that the ViewModel correctly identifies an invalid daily effort that is greater than 24 hours.
     */
    @Test
    fun test_Validate_Inputs_invalid_DailyEffortGreaterThan24() = runBlocking {
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
    /**
     * Tests that the ViewModel correctly identifies an invalid starting date that is greater than the ending date.
     */
    @Test
    fun test_Validate_Inputs_invalid_StartingDateGreaterThanEndingDate() = runBlocking {
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
    /**
     * Tests the following functions of the ViewModel:
     * - addNewItem()
     * - retrieveAllItemsByUserId()
     * - deleteItem()
     */
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