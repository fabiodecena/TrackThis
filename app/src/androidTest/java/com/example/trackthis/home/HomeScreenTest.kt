package com.example.trackthis.home

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.testing.TestNavHostController
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackthis.R
import com.example.trackthis.data.TopicListElement
import com.example.trackthis.data.TopicListRepository
import com.example.trackthis.data.database.tracked_topic.HistoryDatabase
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.data.database.tracked_topic.TrackedTopicDao
import com.example.trackthis.ui.home.HomeScreen
import com.example.trackthis.ui.home.HomeScreenViewModel
import com.example.trackthis.ui.statistics.charts.ChartViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var topicListRepository: TopicListRepository
    private lateinit var context: Context
    private lateinit var testNavController: TestNavHostController
    private lateinit var trackedTopics: List<TrackedTopic>
    private lateinit var trackedTopicDao: TrackedTopicDao
    private lateinit var database: HistoryDatabase
    private lateinit var chartViewModel: ChartViewModel
    private lateinit var testTopics: List<TopicListElement>

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
        topicListRepository = TopicListRepository(context)
        trackedTopicDao = database.trackedTopicDao()
        testNavController = TestNavHostController(context)
        chartViewModel = ChartViewModel()
        // Insert the tracked topics before each test
        runBlocking {
            trackedTopicDao.insert(trackedTopic1)
            trackedTopicDao.insert(trackedTopic2)
            trackedTopicDao.insert(trackedTopic3)
        }
        trackedTopics = runBlocking { trackedTopicDao.getAllItems().first() }
        // Set up a sample state for topics
        testTopics = listOf(
            TopicListElement(R.string.architecture, R.drawable.architecture),
            TopicListElement(R.string.ecology, R.drawable.ecology)
        )
    }

    @After
    fun tearDown() {
        database.close()
    }
    @Test
    fun testHomeScreenTopicCardsAreDisplayed() {
        // Set up the Composable
        composeTestRule.setContent {
            HomeScreen(
                navController = testNavController,
                homeScreenViewModel = HomeScreenViewModel(topicListRepository),
                trackedTopics = trackedTopics
            )
        }

        // Check if each topic is rendered on the screen by verifying the Text and Image
        testTopics.forEach { topic ->
            composeTestRule.onNodeWithText(context.getString(topic.name)).assertIsDisplayed()
            composeTestRule.onNodeWithContentDescription(context.getString(topic.name)).assertExists()
        }
    }
    @Test
    fun testTopicCardButtonClick() {
        // Set up the Composable
        composeTestRule.setContent {
            HomeScreen(
                navController = testNavController,
                homeScreenViewModel = HomeScreenViewModel(topicListRepository),
                trackedTopics = trackedTopics
            )
        }

        // Perform a click action on the button that expands the TopicCard
        composeTestRule.onNodeWithTag("expand_button_${testTopics[0].name}").performClick()

        // Assert that the expanded state causes the Text to change
        composeTestRule.onNodeWithText("Start to Track your Progress about " + context.getString(testTopics[0].name)).assertIsDisplayed()
            .assertIsDisplayed()
    }
}