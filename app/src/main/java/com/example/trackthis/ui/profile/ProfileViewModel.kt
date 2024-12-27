package com.example.trackthis.ui.profile

import android.content.Context
import android.util.Log
import android.widget.Toast
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


    private fun createUserInFirebase(email: String, password: String, navController: NavController, context: Context) {
        val auth = FirebaseAuth.getInstance()

        // Set Firebase locale (optional)
        auth.setLanguageCode(Locale.getDefault().language) // Set with default language code

        auth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                   navController.navigate(NavigationItem.Home.route)
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    fun createAccount(navController: NavController, context: Context) {
        createUserInFirebase(
            email, password, navController, context
        )
    }

    fun logout(context: Context) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        val authStateListener = FirebaseAuth.AuthStateListener {
            if(it.currentUser == null) {
                Log.d("TAG", "User is logged out")
            }
        }
        firebaseAuth.addAuthStateListener(authStateListener)
    }
}