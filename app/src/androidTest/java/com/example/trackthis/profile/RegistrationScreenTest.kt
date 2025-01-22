package com.example.trackthis.profile

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackthis.ui.profile.RegistrationScreen
import com.example.trackthis.ui.profile.RegistrationViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This class contains JUnit tests to verify the functionality of the [RegistrationScreen].
 * It ensures that the screen behaves as expected in different scenarios, such as successful input validation,
 * invalid email input validation, and empty input validation.
 */
@RunWith(AndroidJUnit4::class)
class RegistrationScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val viewModel = RegistrationViewModel()
    private val uiState = viewModel.registrationUiState
    /**
     * This test verifies that the registration form is validated successfully when valid input is provided.
     * It simulates user input for first name, last name, email, and password, and then asserts that
     * the view model is updated correctly and the registration button is enabled.
     */
    @Test
    fun successful_input_Validation() {
        composeTestRule.setContent {
            MaterialTheme {
                RegistrationScreen(registrationViewModel = viewModel)
            }
        }
        // Input first name
        composeTestRule.onNodeWithText("First Name").performTextInput("John")
        composeTestRule.onNodeWithText("Last Name").performTextInput("Doe")
        composeTestRule.onNodeWithText("Email").performTextInput("test@example.com")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")
        // Verify that the view model is updated
        assert(uiState.value.firstName == "John")
        assert(uiState.value.lastName == "Doe")
        assert(uiState.value.email == "test@example.com")
        assert(uiState.value.password == "password123")
        assert(uiState.value.isRegistrationFormValid)
        // Click the register button
        composeTestRule.onNodeWithText("Register").assertIsEnabled()
        assert(uiState.value.isRegistrationFormValid)
    }
    /**
     * This test verifies that the registration form validation fails when an invalid email is provided.
     * It simulates user input with an invalid email address and asserts that the view model is updated
     * correctly and the registration button is not enabled.
     */
    @Test
    fun invalid_email_input_Validation() {
        composeTestRule.setContent {
            MaterialTheme {
                RegistrationScreen(registrationViewModel = viewModel)
            }
        }

        // Input invalid email
        composeTestRule.onNodeWithText("First Name").performTextInput("John")
        composeTestRule.onNodeWithText("Last Name").performTextInput("Doe")
        composeTestRule.onNodeWithText("Email").performTextInput("")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")

        // Verify that the view model is updated
        assert(uiState.value.firstName == "John")
        assert(uiState.value.lastName == "Doe")
        assert(uiState.value.email == "")
        assert(uiState.value.password == "password123")
        assert(!uiState.value.isRegistrationFormValid)
        // Click the register button
        composeTestRule.onNodeWithText("Register").assertIsNotEnabled()
    }
    /**
     * This test verifies that the registration form validation fails when all input fields are empty.
     * It asserts that the view model is initialized with empty values and the registration button is not enabled.
     */
    @Test
    fun empty_input_Validation() {
        composeTestRule.setContent {
            MaterialTheme {
                RegistrationScreen(registrationViewModel = viewModel)
            }
        }

        assert(uiState.value.firstName == "")
        assert(uiState.value.lastName == "")
        assert(uiState.value.email == "")
        assert(uiState.value.password == "")
        assert(!uiState.value.isRegistrationFormValid)

        composeTestRule.onNodeWithText("Register").assertIsNotEnabled()
    }
}