package com.example.trackthis.profile

import com.example.trackthis.ui.profile.RegistrationViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * This class contains JUnit tests to verify the functionality of the `RegistrationViewModel`.
 *
 * These tests cover the following scenarios:
 * - Updating the first name, last name, email, and password should be reflected in the UI state.
 * - Input validation should correctly identify valid and invalid registration form inputs.
 */
class RegistrationViewModelTest {
    private val viewModel = RegistrationViewModel()

    @Test
    fun updating_first_name_should_be_reflected_in_UI_state() = runTest {
        viewModel.updateFirstName("John")

        val state = viewModel.registrationUiState.first()
        assertEquals("John", state.firstName)
    }

    @Test
    fun updating_last_name_should_be_reflected_in_UI_state() = runTest {
        viewModel.updateLastName("Doe")

        val state = viewModel.registrationUiState.first()
        assertEquals("Doe", state.lastName)
    }

    @Test
    fun updating_email_should_be_reflected_in_UI_state() = runTest {
        viewModel.updateEmail("john.doe@example.com")

        val state = viewModel.registrationUiState.first()
        assertEquals("john.doe@example.com", state.email)
    }

    @Test
    fun updating_password_should_be_reflected_in_UI_state() = runTest {
        viewModel.updatePassword("password123")

        val state = viewModel.registrationUiState.first()
        assertEquals("password123", state.password)
    }

    @Test
    fun successful_input_validation() = runTest {
        viewModel.updateFirstName("John")
        viewModel.updateLastName("Doe")
        viewModel.updateEmail("john.doe@example.com")
        viewModel.updatePassword("password123")

        val state = viewModel.registrationUiState.first()
        assertEquals(false, state.isFirstNameError)
        assertEquals(false, state.isLastNameError)
        assertEquals(false, state.isEmailError)
        assertEquals(false, state.isPasswordError)
        assertEquals(true, state.isRegistrationFormValid)
    }

    @Test
    fun invalid_input_validation() = runTest {
        viewModel.updateFirstName("")
        viewModel.updateLastName("")
        viewModel.updateEmail("")
        viewModel.updatePassword("")

        val state = viewModel.registrationUiState.first()
        assertEquals(true, state.isFirstNameError)
        assertEquals(true, state.isLastNameError)
        assertEquals(true, state.isEmailError)
        assertEquals(true, state.isPasswordError)
        assertEquals(false, state.isRegistrationFormValid)
    }
}