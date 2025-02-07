package com.example.trackthis.insert_track

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackthis.R
import com.example.trackthis.data.TopicListElement
import com.example.trackthis.data.database.tracked_topic.HistoryDatabase
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.data.database.tracked_topic.TrackedTopicDao
import com.example.trackthis.ui.insert_track.TrackEntryScreen
import com.example.trackthis.ui.insert_track.TrackEntryViewModel
import com.example.trackthis.ui.statistics.StatisticsScreen
import com.example.trackthis.ui.statistics.charts.ChartViewModel
import com.example.trackthis.ui.statistics.timer.TimerViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * These tests verify the behavior of the TrackEntryScreen when the user interacts with it,
 * such as entering data, submitting the form, and navigating to other screens.
 *
 * The tests use the Jetpack Compose testing framework to simulate user interactions and
 * assert the expected behavior of the UI.
 */
@RunWith(AndroidJUnit4::class)
class TrackEntryScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var context: Context
    private lateinit var testNavController: TestNavHostController
    private lateinit var trackedTopics: List<TrackedTopic>
    private lateinit var trackedTopicDao: TrackedTopicDao
    private lateinit var database: HistoryDatabase
    private lateinit var chartViewModel: ChartViewModel
    private lateinit var timerViewModel: TimerViewModel
    private lateinit var testTopics: List<TopicListElement>
    private lateinit var trackEntryViewModel: TrackEntryViewModel
    private lateinit var startingDate: String
    private lateinit var endingDate: String
    private lateinit var trackedTopic1: TrackedTopic
    private lateinit var trackedTopic2: TrackedTopic
    private lateinit var trackedTopic3: TrackedTopic
    /**
     * Sets up the database and DAO before all tests.
     * It creates an in-memory database to avoid affecting the actual database.
     * It also inserts the tracked topics that will be used by the tests.
     */
    @Before
    fun setupDatabaseAndInsertData() {
        context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, HistoryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        trackedTopicDao = database.trackedTopicDao()
        trackedTopic1 = TrackedTopic(
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
        trackedTopic2 = TrackedTopic(
            id = 2,
            userId = "1f",
            name = 2131492869,
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
            name = 2131492871,
            dailyEffort = 4.0,
            finalGoal = 100,
            startingDate = "01/01/2026",
            endingDate = "01/30/2026",
            totalTimeSpent = 20,
            weeklyTimeSpent = 10,
            dailyTimeSpent = mapOf("Monday" to 4, "Friday" to 6)
        )
        testNavController = TestNavHostController(context)
        timerViewModel = TimerViewModel(trackedTopicDao)
        trackEntryViewModel = TrackEntryViewModel(trackedTopicDao)
        chartViewModel = ChartViewModel()
        // Insert the tracked topics before each test
        runBlocking {
            trackedTopicDao.insert(trackedTopic1)
            trackedTopicDao.insert(trackedTopic2)
            trackedTopicDao.insert(trackedTopic3)
        }
        trackedTopics = runBlocking { trackedTopicDao.getAllItems().first() }
        // Set up a sample state for topics
        testTopics = listOf(TopicListElement(R.string.architecture, R.drawable.architecture))
    }

    @After
    fun tearDown() {
        database.close()
    }
    /**
     * This test verifies that the screen correctly handles valid input,
     * updates the database, and navigates to the relative StatisticsScreen.
     */
    @Test
    fun testTrackEntryScreen_valid_Input_Submission_and_respectively_navigation() {
        startingDate = getCurrentDateFormatted()
        endingDate = getEndingDateFormatted()

        composeTestRule.setContent {
            testNavController.navigatorProvider.addNavigator(ComposeNavigator())

            NavHost(
                navController = testNavController,
                startDestination = "trackDetails"
            ) {
                composable("trackDetails") {
                    TrackEntryScreen(
                        topic = testTopics[0],
                        navController = testNavController,
                        timerViewModel = timerViewModel,
                        trackEntryViewModel = trackEntryViewModel
                    )
                }
                composable("statistics/${trackedTopic1.name}") { backStackEntry ->
                    backStackEntry.arguments?.getString("topicName")
                    StatisticsScreen(
                        timerViewModel = timerViewModel,
                        chartViewModel = chartViewModel,
                        topic = trackedTopic1,
                        navController = testNavController
                    )
                }
            }
        }
        composeTestRule.onNodeWithText(context.getString(testTopics[0].name)).assertIsDisplayed()

        // Fill in the daily effort input
        composeTestRule.onNodeWithText(context.getString(R.string.daily_effort)).performTextInput("2")

        // Fill in the final goal input
        composeTestRule.onNodeWithText(context.getString(R.string.final_goal)).performTextInput("10")

        // Open and select a starting date
        composeTestRule.onNodeWithText(context.getString(R.string.starting_date)).performClick()
        composeTestRule.onAllNodes(hasClickAction()).printToLog("Clickable")
        composeTestRule.onAllNodes(hasNoClickAction()).printToLog("Not Clickable")
        composeTestRule.onNodeWithText(startingDate).performClick()
        composeTestRule.onNodeWithText("OK").performClick()

        // Open and select an ending date
        composeTestRule.onNodeWithText(context.getString(R.string.ending_date)).performClick()
        composeTestRule.onNodeWithText(endingDate).performClick()
        composeTestRule.onNodeWithText("OK").performClick()

        // Click the submit button
        composeTestRule.onNodeWithContentDescription("Submit").assertIsEnabled()
        composeTestRule.onNodeWithContentDescription("Submit").performClick()
        assert(testNavController.currentBackStackEntry?.destination?.route == "statistics/${trackedTopic1.name}")
    }
    /**
     * This test verifies that the screen displays the correct error message
     * when the user enters a daily effort value greater than the final goal.
     */
    @Test
    fun correct_error_message_displayed_when_invalid_InputSubmission_with_dailyEffort_greaterThanFinalGoal() {
        startingDate = getCurrentDateFormatted()
        endingDate = getEndingDateFormatted()
        composeTestRule.setContent {
            TrackEntryScreen(
                topic = testTopics[0],
                navController = testNavController,
                timerViewModel = timerViewModel,
                trackEntryViewModel = trackEntryViewModel
            )
        }
        composeTestRule.onNodeWithText(context.getString(R.string.daily_effort)).performTextInput("20")

        // Fill in the final goal input
        composeTestRule.onNodeWithText(context.getString(R.string.final_goal)).performTextInput("10")
        // Error message should be displayed
        composeTestRule.onNodeWithText(context.getString(R.string.daily_effort_error)).assertIsDisplayed()

        // Open and select a starting date
        composeTestRule.onNodeWithText(context.getString(R.string.starting_date)).performClick()
        composeTestRule.onNodeWithText(startingDate).performClick()
        composeTestRule.onNodeWithText("OK").performClick()

        // Open and select an ending date
        composeTestRule.onNodeWithText(context.getString(R.string.ending_date)).performClick()
        composeTestRule.onNodeWithText(endingDate).performClick()
        composeTestRule.onNodeWithText("OK").performClick()

        // Click the submit button
        composeTestRule.onNodeWithContentDescription("Submit").assertIsNotEnabled()
    }
    /**
     * This test verifies that the screen displays the correct error message
     * when the user enters a daily effort value greater than 24.
     */
    @Test
    fun correct_error_message_displayed_when_invalid_InputSubmission_with_dailyEffort_greaterThan24() {
        startingDate = getCurrentDateFormatted()
        endingDate = getEndingDateFormatted()
        composeTestRule.setContent {
            TrackEntryScreen(
                topic = testTopics[0],
                navController = testNavController,
                timerViewModel = timerViewModel,
                trackEntryViewModel = trackEntryViewModel
            )
        }
        composeTestRule.onNodeWithText(context.getString(R.string.daily_effort)).performTextInput("34")

        // Fill in the final goal input
        composeTestRule.onNodeWithText(context.getString(R.string.final_goal)).performTextInput("72")
        // Error message should be displayed
        composeTestRule.onNodeWithText(context.getString(R.string.daily_effort_cannot_be_greater_than_24_hours)).assertIsDisplayed()

        // Open and select a starting date
        composeTestRule.onNodeWithText(context.getString(R.string.starting_date)).performClick()
        composeTestRule.onNodeWithText(startingDate).performClick()
        composeTestRule.onNodeWithText("OK").performClick()

        // Open and select an ending date
        composeTestRule.onNodeWithText(context.getString(R.string.ending_date)).performClick()
        composeTestRule.onNodeWithText(endingDate).performClick()
        composeTestRule.onNodeWithText("OK").performClick()

        // Click the submit button
        composeTestRule.onNodeWithContentDescription("Submit").assertIsNotEnabled()
    }
    /**
     * This test verifies that the screen displays the correct error message
     * when the user selects a starting date that is greater than the ending date.
     */
    @Test
    fun correct_error_message_displayed_when_invalid_InputSubmission_with_startingDate_greaterThanEndingDate() {
        startingDate = getCurrentDateFormatted()
        endingDate = getEndingDateFormatted()

        composeTestRule.setContent {
            TrackEntryScreen(
                topic = testTopics[0],
                navController = testNavController,
                timerViewModel = timerViewModel,
                trackEntryViewModel = trackEntryViewModel
            )
        }
        composeTestRule.onNodeWithText(context.getString(R.string.daily_effort)).performTextInput("5")
        // Fill in the final goal input
        composeTestRule.onNodeWithText(context.getString(R.string.final_goal)).performTextInput("20")
        // Open and select a starting date
        composeTestRule.onNodeWithText(context.getString(R.string.starting_date)).performClick()
        composeTestRule.onNodeWithText("OK").performClick()
        // Open and select an ending date
        composeTestRule.onNodeWithText(context.getString(R.string.ending_date)).performClick()
        composeTestRule.onNodeWithText("OK").performClick()
        // Error message should be displayed
        composeTestRule.onNodeWithText(context.getString(R.string.dates_error)).assertIsDisplayed()
        // Click the submit button
        composeTestRule.onNodeWithContentDescription("Submit").assertIsNotEnabled()
    }
}

fun getCurrentDateFormatted(): String {
    val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH)
    val currentDate = Date()
    return "Today, ${dateFormat.format(currentDate)}"
}
fun getEndingDateFormatted(): String {
    val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH)
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.add(Calendar.DAY_OF_YEAR, 1) // Add one day
    val nextDate = calendar.time
    return dateFormat.format(nextDate)
}
