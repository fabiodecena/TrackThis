package com.example.trackthis.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackthis.R
import com.example.trackthis.ui.navigation.NavigationItem

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    registrationViewModel: RegistrationViewModel
) {
    val context = LocalContext.current
    val registrationUiState by registrationViewModel.registrationUiState.collectAsState()
    val userName = registrationUiState.userName

    Surface(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.welcome),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = userName,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_small))
                        .align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
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
                    Text(text = stringResource(R.string.register_button))
                }
                Button(
                    onClick = { navController.navigate(NavigationItem.Login.route) }
                ) {
                    Text(text = stringResource(R.string.login_button))
                }
                Button(
                    onClick = { registrationViewModel.logout(context) }
                ) {
                    Text(text = stringResource(R.string.logout_button))
                }
            }
        }
    }
}
