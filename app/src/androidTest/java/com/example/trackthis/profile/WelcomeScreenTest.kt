package com.example.trackthis.profile

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
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackthis.R
import com.example.trackthis.ui.navigation.NavigationItem
import com.example.trackthis.ui.profile.LoginScreen
import com.example.trackthis.ui.profile.RegistrationScreen
import com.example.trackthis.ui.profile.RegistrationViewModel
import com.example.trackthis.ui.profile.WelcomeScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WelcomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val testNavController = TestNavHostController(context)
    private val registrationViewModel = RegistrationViewModel()


    @Before
    fun setup() {
        composeTestRule.setContent {
            testNavController.navigatorProvider.addNavigator(ComposeNavigator())

            NavHost(
                navController = testNavController,
                startDestination = "welcome"
            ) {
                composable("welcome") {
                    WelcomeScreen(
                        registrationViewModel = registrationViewModel,
                        navController = testNavController
                    )
                }
                composable("profile") {
                    RegistrationScreen(
                        registrationViewModel = registrationViewModel
                    )
                }
                composable("login") {
                    LoginScreen()
                }
            }
        }
    }


    @Test
    fun welcomeScreen_displays_UI_and_navigates_to_registration() {
        // Verify welcome text is displayed
        composeTestRule.onNodeWithText(context.getString(R.string.welcome))
            .assertExists()

        // Verify userName is displayed
        if (registrationViewModel.registrationUiState.value.userName != "") {
            composeTestRule.onNodeWithText(registrationViewModel.registrationUiState.value.userName)
                .assertExists()
        } else {
            composeTestRule.onNodeWithText("User not Logged In")
                .assertExists()
        }

        // Verify buttons are displayed and clickable
        composeTestRule.onNodeWithText(context.getString(R.string.register_button))
            .assertExists()
            .performClick()

        // Verify navigation to Registration
        assert(testNavController.currentDestination?.route == NavigationItem.Registration.route)
    }
    @Test
    fun welcomeScreen_displays_UI_and_navigates_to_login() {
        // Verify login button is displayed and clickable
        composeTestRule.onNodeWithText(context.getString(R.string.login_button))
            .assertExists()
            .performClick()
        // Verify navigation to Login
        assert(testNavController.currentDestination?.route == NavigationItem.Login.route)
    }
    @Test
    fun welcomeScreen_displays_logout_button_and_logs_out() {
        val state = registrationViewModel.registrationUiState.value
        composeTestRule.onNodeWithText(state.userName).assertIsDisplayed()
        // Verify logout button is displayed and clickable
        composeTestRule.onNodeWithText(context.getString(R.string.logout_button))
            .assertExists()
            .performClick()
        if (state.userName != "User not Logged In") {
            composeTestRule.onNodeWithText("User is logged out").isDisplayed()
        } else {
            composeTestRule.onNodeWithText("User is already logged out").isDisplayed()
        }
    }
}