package com.example.trackthis.profile

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackthis.ui.profile.LoginViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginViewModelTest {
    private val loginViewModel = LoginViewModel()

    @Test
    fun email_is_updated_then_ui_state_should_reflect_the_change() = runTest {
        val testEmail = "test@example.com"
        loginViewModel.updateEmail(testEmail)
        val uiState = loginViewModel.loginUiState.first()
        Assert.assertEquals(testEmail, uiState.email)
    }

    @Test
    fun password_is_updated_then_ui_state_should_reflect_the_change() = runTest {
        val testPassword = "password123"
        loginViewModel.updatePassword(testPassword)
        val uiState = loginViewModel.loginUiState.first()
        Assert.assertEquals(testPassword, uiState.password)
    }

    @Test
    fun email_blank_and_isEmailError_should_be_true() = runTest {
        loginViewModel.updateEmail("")
        val uiState = loginViewModel.loginUiState.first()
        Assert.assertEquals(true, uiState.isEmailError)
    }

    @Test
    fun blank_password_and_isPasswordError_should_be_true() = runTest {
        loginViewModel.updatePassword("")
        val uiState = loginViewModel.loginUiState.first()
        Assert.assertEquals(true, uiState.isPasswordError)
    }

    @Test
    fun email_and_password_are_not_blank_then_isLoginFormValid_should_be_true() = runTest {
        loginViewModel.updateEmail("test@example.com")
        loginViewModel.updatePassword("password123")
        val uiState = loginViewModel.loginUiState.first()
        Assert.assertEquals(true, uiState.isLoginFormValid)
    }
}