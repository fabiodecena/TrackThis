package com.example.trackthis.statistics

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackthis.data.database.tracked_topic.HistoryDatabase
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.data.database.tracked_topic.TrackedTopicDao
import com.example.trackthis.ui.statistics.StatisticsScreen
import com.example.trackthis.ui.statistics.charts.ChartViewModel
import com.example.trackthis.ui.statistics.charts.dailyEffortList
import com.example.trackthis.ui.statistics.charts.pointsData
import com.example.trackthis.ui.statistics.timer.TimerViewModel
import com.example.trackthis.ui.statistics.timer.formatTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This class contains JUnit tests to verify the functionality of the [StatisticsScreen].
 * It tests the UI elements displayed on the screen, the behavior when the tracked topic is null,
 * the updating of chart data with database data, and the timer's value reflecting daily time spent.
 */
@RunWith(AndroidJUnit4::class)
class StatisticsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var chartViewModel: ChartViewModel
    private lateinit var timerViewModel: TimerViewModel
    private lateinit var navController: TestNavHostController
    private lateinit var database: HistoryDatabase
    private lateinit var trackedTopicDao: TrackedTopicDao
    private lateinit var context: Context
    private lateinit var trackedTopic: TrackedTopic

    @Before
    fun setup() {
        // Initialize ViewModels with real data sources or fake in-memory data sources
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            HistoryDatabase::class.java
        ).build()
        trackedTopicDao = database.trackedTopicDao()
        context = ApplicationProvider.getApplicationContext()
        chartViewModel = ChartViewModel()
        timerViewModel = TimerViewModel(trackedTopicDao)
        navController = TestNavHostController(context)
        trackedTopic = TrackedTopic(
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
        runTest {
            trackedTopicDao.insert(trackedTopic)
        }
    }
    @After
    fun tearDown() {
        database.close()
    }
    /**
     * Tests that the StatisticsScreen displays the correct UI elements for a given tracked topic.
     * It verifies that the topic name, minimum daily effort, progress, days of the week, timer,
     * and control buttons (Pause, Stop, Refresh) are displayed.
     */
    @Test
    fun statisticsScreen_displays_properly_UI_elements_of_relative_tracked_topic() {
        composeTestRule.setContent {
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            NavHost(
                navController = navController,
                startDestination = "statistics"
            ) {
                composable("statistics") {
                    StatisticsScreen(
                        chartViewModel = chartViewModel,
                        timerViewModel = timerViewModel,
                        topic = trackedTopic,
                        navController = navController,
                    )
                }
            }
        }
        runTest {
            // Assert that the topic name is displayed
            composeTestRule.onNodeWithText(context.getString(trackedTopicDao.getAllItems().first().first().name)).assertIsDisplayed()
            // verify that all UI elements are displayed
            composeTestRule.onNodeWithText("Min Daily Effort").assertIsDisplayed()
            composeTestRule.onNodeWithText("Progress").assertIsDisplayed()
            composeTestRule.onNodeWithText("M").assertIsDisplayed()
            composeTestRule
                .onAllNodesWithText("T")
                .assertCountEquals(2)
            composeTestRule.onNodeWithText("W").assertIsDisplayed()
            composeTestRule.onNodeWithText("F").assertIsDisplayed()
            composeTestRule
                .onAllNodesWithText("S")
                .assertCountEquals(2)
            composeTestRule.onNodeWithText(timerViewModel.timerUiState.value.timer.formatTime()).assertIsDisplayed()
            composeTestRule.onNodeWithText("Pause").assertIsDisplayed()
            composeTestRule.onNodeWithText("Stop").assertIsDisplayed()
            composeTestRule.onNodeWithText("Refresh").assertIsDisplayed()
        }
    }
    /**
     * Tests that the StatisticsScreen displays no content when the tracked topic is null.
     * It verifies that the topic name is not displayed in this case.
     */
    @Test
    fun statisticsScreen_no_Content_displayed_if_tracked_topic_is_null() {
        runTest {
            composeTestRule.setContent {
                navController.navigatorProvider.addNavigator(ComposeNavigator())

                NavHost(
                    navController = navController,
                    startDestination = "statistics"
                ) {
                    composable("statistics") {
                        StatisticsScreen(
                            chartViewModel = chartViewModel,
                            timerViewModel = timerViewModel,
                            topic = null,
                            navController = navController,
                        )
                    }
                }
            }
            composeTestRule.onNodeWithText(context.getString(trackedTopicDao.getAllItems().first().first().name)).assertIsNotDisplayed()
        }
    }
    /**
     * Tests that the chart data is updated with data from the database.
     * It verifies that the chartViewModel's chartUiState contains the expected points data and daily effort list.
     */
    @Test
    fun verify_chart_data_are_updated_with_database_data() = runTest {
        composeTestRule.setContent {
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            NavHost(
                navController = navController,
                startDestination = "statistics"
            ) {
                composable("statistics") {
                    StatisticsScreen(
                        chartViewModel = chartViewModel,
                        timerViewModel = timerViewModel,
                        topic = trackedTopic,
                        navController = navController,
                    )
                }
            }
        }
        assert(chartViewModel.chartUiState.value.defaultPointsData == pointsData)
        assert(chartViewModel.chartUiState.value.dailyEffort == dailyEffortList)
    }
    /**
     * Tests that the timer value reflects the dailyTimeSpent value and updates after a delay.
     * It verifies that the timer starts, runs for a specific duration, and updates its displayed value.
     */
    @Test
    fun verify_timer_value_reflect_dailyTimeSpent_value_and_update_a_delay_input() = runTest {
        composeTestRule.setContent {
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            NavHost(
                navController = navController,
                startDestination = "statistics"
            ) {
                composable("statistics") {
                    StatisticsScreen(
                        chartViewModel = chartViewModel,
                        timerViewModel = timerViewModel,
                        topic = trackedTopic,
                        navController = navController,
                    )
                }
            }
        }
        val initialTimer = timerViewModel.timerUiState.value.timer
        val currentTimerValue = initialTimer.formatTime()
        composeTestRule.onNodeWithText(currentTimerValue).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Start Timer").performClick()
        runBlocking {
            delay(5000)
        }
        val updatedTimerValue = (initialTimer + 5).formatTime()
        composeTestRule.onNodeWithText(updatedTimerValue).assertIsDisplayed()
    }
}