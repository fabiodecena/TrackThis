package com.example.trackthis.ui.profile

/**
 * This data class represents the UI state for the [RegistrationScreen], [LoginScreen] and [WelcomeScreen]
 * Data and logic are managed by the corresponding view model: [RegistrationViewModel], [LoginViewModel].
 *
 * @param firstName The first name entered by the user.
 * @param lastName The last name entered by the user.
 * @param email The email address entered by the user.
 * @param password The password entered by the user.
 * @param isFirstNameError Indicates if there is an error with the first name input.
 * @param isLastNameError Indicates if there is an error with the last name input.
 * @param isEmailError Indicates if there is an error with the email input.
 * @param isPasswordError Indicates if there is an error with the password input.
 * @param isRegistrationFormValid Indicates if the registration form is valid.
 * @param isLoginFormValid Indicates if the login form is valid.
 * @param userName The username entered by the user.
 */
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