package com.example.trackthis.ui.profile

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * This class represents the view model for the login screen.
 * It manages the UI state for the login form, including `email` and `password` inputs,
 * validation, and the login process using Firebase Authentication [FirebaseAuth].
 */
class LoginViewModel : ViewModel() {
    /**
    *  A [StateFlow] that emits the current [ProfileUiState].
    *  It is used to observe and update the UI based on the current state.
    */
    private val _loginUiState = MutableStateFlow(ProfileUiState())
    val loginUiState: StateFlow<ProfileUiState> = _loginUiState.asStateFlow()

    fun updateEmail(email: String) {
        _loginUiState.value = _loginUiState.value.copy(email = email)
        validateInputs()
    }

    fun updatePassword(password: String) {
        _loginUiState.value = _loginUiState.value.copy(password = password)
        validateInputs()
    }
    /**
     * Validates the current email and password inputs.
     * Updates the [ProfileUiState] with the validation results,
     * setting error flags and the overall form validity.
     */
    private fun validateInputs() {
        val isEmailError = _loginUiState.value.email.isBlank()
        val isPasswordError = _loginUiState.value.password.isBlank()
        val isFormValid = !isEmailError && !isPasswordError

        _loginUiState.value = _loginUiState.value.copy(
            isEmailError = isEmailError,
            isPasswordError = isPasswordError,
            isLoginFormValid = isFormValid
        )
    }
    /**
     * Attempts to log in a user with the provided email and password using Firebase Authentication.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @param context The context used to display a toast and restart the app.
     */
    fun loginInFirebase(email: String, password: String, context: Context) {
        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("TAG", "Login successful")
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    // Restart the app to clear undesired data and views for a specific user
                    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
    }
}