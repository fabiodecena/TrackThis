package com.example.trackthis.ui.profile

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

/**
 * This class represents the view model for the registration screen.
 * It manages the UI state for the registration form, including user input,
 * validation, and interactions with Firebase authentication [FirebaseAuth].
 */
class RegistrationViewModel : ViewModel() {
    /**
     * A [StateFlow] that holds the current UI state for the registration screen.
     * It includes user input fields, validation flags, and overall form validity.
     */
    private val _registrationUiState = MutableStateFlow(ProfileUiState())
    val registrationUiState: StateFlow<ProfileUiState> = _registrationUiState.asStateFlow()

    private val auth = Firebase.auth

    init {
        fetchUserName()
    }
    /**
     * Fetches the current user's email from Firebase and updates the UI state.
     * If the user is not logged in, it sets the username to "User not Logged In".
     */
    private fun fetchUserName() {
        val user = auth.currentUser
        _registrationUiState.value = _registrationUiState.value.copy(
            userName = user?.email ?: "User not Logged In"
        )
    }

    fun updateFirstName(firstName: String) {
        _registrationUiState.value = _registrationUiState.value.copy(firstName = firstName)
        validateInputs()
    }

    fun updateLastName(lastName: String) {
        _registrationUiState.value = _registrationUiState.value.copy(lastName = lastName)
        validateInputs()
    }

    fun updateEmail(email: String) {
        _registrationUiState.value = _registrationUiState.value.copy(email = email)
        validateInputs()
    }

    fun updatePassword(password: String) {
        _registrationUiState.value = _registrationUiState.value.copy(password = password)
        validateInputs()
    }
    /**
     * Validates all input fields in the UI state and updates the error flags and form validity.
     * It checks if first name, last name, email, and password fields are blank.
     */
    private fun validateInputs() {
        val isFirstNameError = _registrationUiState.value.firstName.isBlank()
        val isLastNameError = _registrationUiState.value.lastName.isBlank()
        val isEmailError = _registrationUiState.value.email.isBlank()
        val isPasswordError = _registrationUiState.value.password.isBlank()
        val isFormValid = !isFirstNameError && !isLastNameError && !isEmailError && !isPasswordError

        _registrationUiState.value = _registrationUiState.value.copy(
            isFirstNameError = isFirstNameError,
            isLastNameError = isLastNameError,
            isEmailError = isEmailError,
            isPasswordError = isPasswordError,
            isRegistrationFormValid = isFormValid
        )
    }
    /**
     * Creates a new user in Firebase with the provided email and password.
     * It also sets the Firebase locale to the device's default language.
     * @param email The email of the new user.
     * @param password The password of the new user.
     * @param context The context used to display toast messages and restart the app.
     */
    private fun createUserInFirebase(email: String, password: String, context: Context) {
        val auth = FirebaseAuth.getInstance()

        auth.setLanguageCode(Locale.getDefault().language)

        auth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "User created successfully", Toast.LENGTH_SHORT).show()
                    // Restart the app
                    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    fun createAccount(context: Context) {
        createUserInFirebase(
            _registrationUiState.value.email, _registrationUiState.value.password, context
        )
    }
    /**
     * This function is called when the user logs out by clicking the "Logout" button in [WelcomeScreen].
     * It signs the user out of Firebase and restarts the app to clear the session.
     * @param context The context used to display toast messages and restart the app.
     */
    fun logout(context: Context) {
        val firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser != null) {
            firebaseAuth.signOut()
            Toast.makeText(context, "User is logged out", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "User is already logged out", Toast.LENGTH_SHORT).show()
        }
        // Restart the app
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}