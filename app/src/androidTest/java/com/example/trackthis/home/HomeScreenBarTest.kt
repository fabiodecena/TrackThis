package com.example.trackthis.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackthis.MainActivity
import com.example.trackthis.ui.navigation.NavigationItem
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationBarTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun topAppBar_navigatesToSettingsScreen() {
        composeTestRule.onNodeWithContentDescription(NavigationItem.Settings.title).performClick()
        composeTestRule.onNodeWithText("Active Tracking").assertIsDisplayed()
        composeTestRule.onNodeWithText("Inactive Tracking").assertIsDisplayed()
    }

    @Test
    fun topAppBar_navigatesToWelcomeScreen_and_navigation_to_Registration_Screen() {
        composeTestRule.onNodeWithText(NavigationItem.Welcome.title).performClick()
        composeTestRule.onNodeWithText("Register").assertIsDisplayed()
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
        composeTestRule.onNodeWithText("Logout").assertIsDisplayed()
        composeTestRule.onNodeWithText("Welcome").assertIsDisplayed()

        composeTestRule.onNodeWithText("Register").performClick()
        composeTestRule.onNodeWithText("First Name").assertIsDisplayed()
    }

    @Test
    fun topAppBar_navigatesToWelcomeScreen_and_navigation_to_Login_Screen() {
        composeTestRule.onNodeWithText(NavigationItem.Welcome.title).performClick()

        composeTestRule.onNodeWithText("Login").performClick()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
    }

    @Test
    fun topAppBar_navigatesToWelcomeScreen_and_logout() {
        composeTestRule.onNodeWithText(NavigationItem.Welcome.title).performClick()

        composeTestRule.onNodeWithText("Logout").performClick()
        composeTestRule.onNodeWithText("User not Logged In").assertIsDisplayed()
        composeTestRule.onNodeWithText("Logout").performClick()
        // Verify that Toast is displayed if user is already logged out
        composeTestRule.onNodeWithText("User is already logged out").isDisplayed()
    }

    @Test
    fun bottomBar_navigatesToHomeScreen() {
        composeTestRule.onNodeWithText(NavigationItem.Home.title).performClick()
        composeTestRule.onNodeWithText("Home").assertIsSelected()
    }

    @Test
    fun bottomBar_navigatesToStatisticsScreen() {
        composeTestRule.onNodeWithText(NavigationItem.Statistics.title).performClick()
        composeTestRule.onNodeWithText("Statistics").assertIsSelected()
    }

    @Test
    fun bottomBar_navigatesToHistoryScreen() {
        composeTestRule.onNodeWithText(NavigationItem.History.title).performClick()
        composeTestRule.onNodeWithText("History").assertIsSelected()
    }
}