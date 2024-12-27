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

class LoginViewModel : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    fun isLoginValid(): Boolean {
        return  email.isNotBlank() &&
                password.isNotBlank()
    }

    fun loginInFirebase(email: String, password: String, navController: NavController, context: Context) {
        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("TAG", "Login successful")
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    navController.navigate(NavigationItem.Home.route)
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
    }
}