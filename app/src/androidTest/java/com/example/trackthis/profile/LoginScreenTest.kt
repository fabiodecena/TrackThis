package com.example.trackthis.profile

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackthis.ui.profile.LoginScreen
import com.example.trackthis.ui.theme.TrackThisTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This class contains JUnit tests to verify the functionality of the [LoginScreen].
 */
@RunWith(AndroidJUnit4::class)
class LoginScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_successful_Login() {
        composeTestRule.setContent {
            TrackThisTheme  {
                LoginScreen()
            }
        }
        val emailField = composeTestRule.onNodeWithText("Email")
        val passwordField = composeTestRule.onNodeWithText("Password")
        val loginButton = composeTestRule.onNodeWithText("Login")
        // Check that the email field is present and can be interacted with
        emailField.assertExists()
        emailField.performTextInput("fabiodecena@gmail.com")
        loginButton.assertIsNotEnabled()
        // Check that the password field is present and can be interacted with.
        passwordField.assertExists()
        passwordField.performTextInput("123456")
        // Check that the Login button is present and now enabled
        loginButton.assertExists()
        loginButton.assertIsEnabled()
        // Perform click on login button and toast has to be displayed
        loginButton.performClick()
        composeTestRule.onNodeWithText("Login successful").isDisplayed()
        // Waiting for the logout button to be displayed
        runBlocking {
            delay(10000)
        }
        composeTestRule.onNodeWithText("Logout").performClick()
    }
    @Test
    fun loginScreen_invalid_Login() {
        composeTestRule.setContent {
            TrackThisTheme  {
                LoginScreen()
            }
        }
        val emailField = composeTestRule.onNodeWithText("Email")
        val passwordField = composeTestRule.onNodeWithText("Password")
        val loginButton = composeTestRule.onNodeWithText("Login")
        // Check that the email field is present and can be interacted with
        emailField.assertExists()
        emailField.performTextInput("test@example.com")
        loginButton.assertIsNotEnabled()
        // Check that the password field is present and can be interacted with.
        passwordField.assertExists()
        passwordField.performTextInput("123456")
        // Check that the Login button is present and now enabled
        loginButton.assertExists()
        loginButton.assertIsEnabled()
        // Perform click on login button and toast has to be displayed
        loginButton.performClick()
        composeTestRule.onNodeWithText("Invalid email or password").isDisplayed()
    }
}