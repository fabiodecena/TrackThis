package com.example.trackthis.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.trackthis.R
import com.example.trackthis.ui.insert_track.EditField

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    registrationViewModel: RegistrationViewModel
) {
    val registrationUiState by registrationViewModel.registrationUiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium2)),
            label = R.string.first_name,
            leadingIcon = Icons.Filled.AccountCircle,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            value = registrationUiState.firstName,
            onValueChanged = {
                registrationViewModel.updateFirstName(it)
            },
            isError = registrationUiState.isFirstNameError
        )
        EditField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium2)),
            label = R.string.last_name,
            leadingIcon = Icons.Filled.AccountCircle,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            value = registrationUiState.lastName,
            onValueChanged = {
                registrationViewModel.updateLastName(it)
            },
            isError = registrationUiState.isLastNameError
        )
        EditField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium2)),
            label = R.string.email,
            leadingIcon = Icons.Filled.Email,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            value = registrationUiState.email,
            onValueChanged = {
                registrationViewModel.updateEmail(it)
            },
            isError = registrationUiState.isEmailError
        )
        EditFieldPassword(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium2)),
            label = R.string.password,
            leadingIcon = Icons.Filled.Lock,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            value = registrationUiState.password,
            onValueChanged = {
                registrationViewModel.updatePassword(it)
            },
            isError = registrationUiState.isPasswordError,
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = { registrationViewModel.createAccount(context) },
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium2))
                .align(Alignment.End),
            enabled = registrationUiState.isRegistrationFormValid
        ) {
            Text("Register")
        }
    }
}