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


class RegistrationViewModel : ViewModel() {
    private val _registrationUiState = MutableStateFlow(ProfileUiState())
    val registrationUiState: StateFlow<ProfileUiState> = _registrationUiState.asStateFlow()

    private val auth = Firebase.auth

    init {
        fetchUserName()
    }

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

    private fun createUserInFirebase(email: String, password: String, context: Context) {
        val auth = FirebaseAuth.getInstance()

        // Set Firebase locale (optional)
        auth.setLanguageCode(Locale.getDefault().language) // Set with default language code

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