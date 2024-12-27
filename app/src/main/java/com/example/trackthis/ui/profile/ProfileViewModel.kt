package com.example.trackthis.ui.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.trackthis.ui.navigation.NavigationItem
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale


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


    private fun createUserInFirebase(email: String, password: String, navController: NavController) {
        val auth = FirebaseAuth.getInstance()

        // Set Firebase locale (optional)
        auth.setLanguageCode(Locale.getDefault().language) // Replace "en" with desired language code

        auth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d("TAG", "createUserInFirebase: ${it.isSuccessful}")
                if (it.isSuccessful) {
                   navController.navigate(NavigationItem.Home.route)
                }
            }
            .addOnFailureListener {
                Log.d("TAG", "createUserInFirebase: ${it.message}")
            }
    }

    fun createAccount(navController: NavController) {
        createUserInFirebase(
            email, password, navController
        )
    }
}