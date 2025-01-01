package com.example.trackthis.ui.profile

data class ProfileUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val isFirstNameError: Boolean = false,
    val isLastNameError: Boolean = false,
    val isEmailError: Boolean = false,
    val isPasswordError: Boolean = false,
    val isRegistrationFormValid: Boolean = false,
    val isLoginFormValid: Boolean = false,
    val userName: String = ""
)