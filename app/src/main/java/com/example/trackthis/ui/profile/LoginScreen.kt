package com.example.trackthis.ui.profile

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackthis.R
import com.example.trackthis.ui.insert_track.EditField

/**
 * This composable displays a login form with fields for `email` and `password`.
 * It uses a [LoginViewModel] to manage the state of the login form and handle the login logic.
 *
 * @param modifier Modifier for styling the layout.
 * @param loginViewModel ViewModel for managing the login state and logic.
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel()
) {
    val loginUiState by loginViewModel.loginUiState.collectAsState()
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
            label = R.string.email,
            leadingIcon = Icons.Filled.Email,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            value = loginUiState.email,
            onValueChanged = {
               loginViewModel.updateEmail(it)
            },
            isError = loginUiState.isEmailError
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
            value = loginUiState.password,
            onValueChanged = {
                loginViewModel.updatePassword(it)
            },
            isError = loginUiState.isPasswordError,
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = { loginViewModel.loginInFirebase(loginUiState.email, loginUiState.password, context) },
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium2))
                .align(Alignment.End),
            enabled = loginUiState.isLoginFormValid
        ) {
            Text("Login")
        }
    }
}
/**
 * This composable displays a text field for `password` input, with a leading icon,
 * error indication, and password visual transformation.
 *
 * @param modifier Modifier for styling the layout.
 * @param label String resource for the text field label.
 * @param leadingIcon Vector image for the leading icon.
 * @param keyboardOptions Keyboard options for the text field.
 * @param value Current value of the text field.
 * @param onValueChanged Callback for value changes in the text field.
 * @param isError Boolean indicating if the text field has an error. The logic is managed with [LoginViewModel]
 * @param visualTransformation Visual transformation for the password field to hide the characters with '*'.
 */
@Composable
fun EditFieldPassword(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    leadingIcon: ImageVector,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    isError: Boolean,
    visualTransformation: PasswordVisualTransformation
) {
    val borderColor = if (isError) {
        Color.Red
    } else {
        Color.Transparent // Or your default border color
    }
    TextField(
        visualTransformation = visualTransformation,
        value = value,
        singleLine = true,
        leadingIcon = { Icon(leadingIcon,null) },
        modifier = modifier,
        onValueChange = onValueChanged,
        label = { Text(stringResource(label)) },
        keyboardOptions = keyboardOptions,
        shape = MaterialTheme.shapes.large,
        colors = TextFieldDefaults.colors(
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = borderColor,
            unfocusedIndicatorColor = borderColor
        )
    )
}