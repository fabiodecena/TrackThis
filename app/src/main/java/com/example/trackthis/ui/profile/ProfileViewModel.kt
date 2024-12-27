package com.example.trackthis.ui.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth


class ProfileViewModel : ViewModel()  {
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")


    fun isFormValid(): Boolean {
        return firstName.isNotBlank() &&
                lastName.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank()
    }


    private fun createUserInFirebase(email: String, password: String) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d("TAG", "createUserInFirebase: ${it.isSuccessful}")
            }
            .addOnFailureListener {
                Log.d("TAG", "createUserInFirebase: ${it.message}")
            }
    }

    fun createAccount() {
        createUserInFirebase(
            email, password
        )
    }
}