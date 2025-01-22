package com.example.trackthis.settings

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackthis.data.TopicListRepository
import com.example.trackthis.ui.navigation.trackNavigationItems
import com.example.trackthis.ui.settings.SettingsScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This class contains JUnit tests to verify the functionality of the [SettingsScreen].
 * It ensures that the screen displays the expected navigation items,
 * and that the toggle selection behavior for active and inactive tracking sections works correctly.
 */
@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val topicListRepository = TopicListRepository(context)
    /**
     * Tests the following scenarios:
     * 1. All navigation items are displayed on the SettingsScreen.
     * 2. An active tracking element is displayed in the Active Tracking Section.
     * 3. The same element is not displayed in the Inactive Tracking Section when active.
     * 4. After an active topic is clicked, it is moved to and displayed in the Inactive Tracking Section.
     */
    @Test
    fun settingsScreen_display_items_and_verify_toggle_selection() {
        runTest {
            composeTestRule.setContent {
                SettingsScreen()
            }
            trackNavigationItems.forEach { item ->
                // Check if each navigation item is displayed
                composeTestRule.onNodeWithText(item.title).assertIsDisplayed()
            }
            // verify that an active element is displayed properly in the Active Tracking Section
            composeTestRule.onNodeWithText(trackNavigationItems[0].title).performClick()
            if (topicListRepository.selectedTopics.first().isNotEmpty()) {
                composeTestRule.onNodeWithText(context.getString(topicListRepository.selectedTopics.first().first().toInt())).assertIsDisplayed()
            }
            // verify that the same element is not displayed in the Inactive Tracking Section
            composeTestRule.onNodeWithText(trackNavigationItems[1].title).performClick()
            if (topicListRepository.selectedTopics.first().isNotEmpty()) {
                composeTestRule.onNodeWithText(context.getString(topicListRepository.selectedTopics.first().first().toInt())).assertIsNotDisplayed()
            }
            // verify that after an active topic is clicked within the Active Tracking Section, is then displayed within the Inactive Tracking Section
            composeTestRule.onNodeWithText(trackNavigationItems[0].title).performClick()
            if (topicListRepository.selectedTopics.first().isNotEmpty()) {
                val selectedTopic = context.getString(topicListRepository.selectedTopics.first().first().toInt())
                composeTestRule.onNodeWithText(context.getString(topicListRepository.selectedTopics.first().first().toInt())).performClick()
                composeTestRule.onNodeWithText(trackNavigationItems[1].title).performClick()
                composeTestRule.onNodeWithText(selectedTopic).assertIsDisplayed()
            }
        }
    }
}