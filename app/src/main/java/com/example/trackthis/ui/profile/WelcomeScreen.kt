package com.example.trackthis.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackthis.ui.navigation.NavigationItem

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
    loginViewModel: LoginViewModel
) {
    val context = LocalContext.current

    Surface(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Welcome",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.headlineLarge
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { navController.navigate(NavigationItem.Registration.route) }
                ) {
                    Text(text = "Register")
                }
                Button(
                    onClick = { navController.navigate(NavigationItem.Login.route) }
                ) {
                    Text(text = "Login")
                }
                Button(
                    onClick = { registrationViewModel.logout(context) }
                ) {
                    Text(text = "Logout")
                }
            }
        }
    }
}
