package com.example.trackthis.history

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import androidx.room.Room
import androidx.room.Transaction
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackthis.data.database.tracked_topic.HistoryDatabase
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.data.database.tracked_topic.TrackedTopicDao
import com.example.trackthis.ui.history.HistoryScreen
import com.example.trackthis.ui.insert_track.TrackEntryViewModel
import com.example.trackthis.ui.statistics.StatisticsScreen
import com.example.trackthis.ui.statistics.charts.ChartViewModel
import com.example.trackthis.ui.statistics.timer.TimerViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HistoryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var database: HistoryDatabase
    private lateinit var trackedTopicDao: TrackedTopicDao
    private lateinit var trackEntryViewModel: TrackEntryViewModel
    private lateinit var timerViewModel: TimerViewModel
    private lateinit var chartViewModel: ChartViewModel
    private lateinit var testNavController: TestNavHostController
    private lateinit var trackedTopics: List<TrackedTopic>
    private lateinit var context: Context

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
                name = 2131492946,
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
                name = 2131492995,
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
                name = 2131492950,
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

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        // Create an in-memory database for testing
        database = Room.inMemoryDatabaseBuilder(context, HistoryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        @Transaction
        trackedTopicDao = database.trackedTopicDao()
        testNavController = TestNavHostController(context)
        trackEntryViewModel = TrackEntryViewModel(trackedTopicDao)
        timerViewModel = TimerViewModel(trackedTopicDao)
        chartViewModel = ChartViewModel()
        // Insert the tracked topics before each test
        runBlocking {
            trackedTopicDao.insert(trackedTopic1)
            trackedTopicDao.insert(trackedTopic2)
            trackedTopicDao.insert(trackedTopic3)
        }
        trackedTopics = runBlocking { trackedTopicDao.getAllItems().first() }
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun historyScreen_displaysTrackedTopics() {
        composeTestRule.setContent {
            HistoryScreen(
                trackEntryViewModel = trackEntryViewModel,
                timerViewModel = timerViewModel,
                trackedTopics = trackedTopics,
                navigateOnSelectedClick = {
                },
                navController = testNavController
            )
        }
        composeTestRule.onNodeWithText(context.getString(trackedTopic1.name)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(trackedTopic2.name)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(trackedTopic3.name)).assertIsDisplayed()
    }
    @Test
    fun historyScreen_click_on_Topic_navigates_to_Statistics_Screen() {
        // Initialize the NavController with a programmatically defined NavGraph
        composeTestRule.setContent {
            testNavController.navigatorProvider.addNavigator(ComposeNavigator())

            NavHost(
                navController = testNavController,
                startDestination = "history"
            ) {
                composable("history") {
                    HistoryScreen(
                        trackEntryViewModel = trackEntryViewModel,
                        timerViewModel = timerViewModel,
                        trackedTopics = trackedTopics,
                        navigateOnSelectedClick = { topicName ->
                            testNavController.navigate("statistics_screen/$topicName")
                        },
                        navController = testNavController
                    )
                }
                composable("statistics_screen/${trackedTopic1.name}") { backStackEntry ->
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

        // Simulate a click on the "Design" Topic with string resource ID 2131492946
        composeTestRule.onNodeWithText(context.getString(trackedTopic1.name)).performClick()

        // Verify that the navigation occurred
        assertEquals("statistics_screen/${trackedTopic1.name}", testNavController.currentDestination?.route)
    }
    @Test
    fun historyScreen_click_on_Topic_when_timerIsRunning_shows_Toast() {
        runBlocking {
            timerViewModel.startTimer()
        }
        // Initialize the NavController with a programmatically defined NavGraph
        composeTestRule.setContent {
            testNavController.navigatorProvider.addNavigator(ComposeNavigator())

            NavHost(
                navController = testNavController,
                startDestination = "history"
            ) {
                composable("history") {
                    HistoryScreen(
                        trackEntryViewModel = trackEntryViewModel,
                        timerViewModel = timerViewModel,
                        trackedTopics = trackedTopics,
                        navigateOnSelectedClick = {
                            testNavController.navigate("history")
                        },
                        navController = testNavController
                    )
                }
            }
        }

        // Simulate a click on the "Design" Topic with string resource ID 2131492946
        composeTestRule.onNodeWithText(context.getString(trackedTopic1.name)).performClick()
        // Verify that the Toast is displayed
        composeTestRule.onNodeWithText("The Timer is Running! Navigation between Topics is Disabled!!!").isDisplayed()
        // Verify that the navigation doesn't occurred
        assertEquals("history", testNavController.currentDestination?.route)
    }
}

