package com.example.trackthis.ui.insert_track

import android.icu.text.SimpleDateFormat
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.example.trackthis.R
import com.example.trackthis.data.Topic
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.ui.navigation.NavigationItem
import com.example.trackthis.ui.statistics.timer.TimerViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun TrackDetails(
    modifier: Modifier = Modifier,
    topic: Topic,
    navController: NavController,
    timerViewModel: TimerViewModel,
    trackEntryViewModel: TrackEntryViewModel
) {
    val trackEntryUiState by trackEntryViewModel.trackEntryUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium)),
            text = stringResource(topic.name),
            style = MaterialTheme.typography.headlineMedium
        )

        Divider()
        Row (
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium2)),
        ) { }
        if (trackEntryUiState.isDailyEffortError) {
            Text(
                text = stringResource(R.string.daily_effort_error),
                color = Color.Red,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
            )
        }
        else if (trackEntryUiState.isDateError) {
            Text(
                text = stringResource(R.string.dates_error),
                color = Color.Red,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
            )
        }
        else if (userId == null) {
            Text(
                text = stringResource(R.string.user_not_logged),
                color = Color.Red,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
            )
        }
        EditField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium2)),
            label = R.string.daily_effort,
            leadingIcon = Icons.Filled.AccessTime,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = trackEntryUiState.dailyEffort,
            onValueChanged = { trackEntryViewModel.updateDailyEffort(it) },
            isError = trackEntryUiState.isDailyEffortError
        )
        EditField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium2)),
            label = R.string.final_goal,
            leadingIcon = Icons.Filled.CheckCircleOutline,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = trackEntryUiState.finalGoal,
            onValueChanged = { trackEntryViewModel.updateFinalGoal(it) },
            isError = trackEntryUiState.isDailyEffortError
        )
        DatePickerFieldToModal(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium2)),
            label = R.string.starting_date,
            onValueChanged = { trackEntryViewModel.updateStartingDate(it) },
            isStartDatePicker = true,
            startingDate = trackEntryUiState.startingDate,
            isError = trackEntryUiState.isDateError
        )
        DatePickerFieldToModal(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium2)),
            label = R.string.ending_date,
            onValueChanged = { trackEntryViewModel.updateEndingDate(it) },
            isStartDatePicker = false,
            startingDate = trackEntryUiState.startingDate,
            isError = trackEntryUiState.isDateError
        )
        Button(
            onClick = {
                timerViewModel.resetTimer()
                timerViewModel.resetData()
                coroutineScope.launch {
                    if (userId != null)
                    trackEntryViewModel.addNewItem(
                        TrackedTopic(
                            userId = userId,
                            name = topic.name,
                            dailyEffort = trackEntryUiState.dailyEffort.toDouble(),
                            finalGoal = trackEntryUiState.finalGoal.toInt(),
                            startingDate = trackEntryUiState.startingDate,
                            endingDate = trackEntryUiState.endingDate,
                            totalTimeSpent = 0,
                            weeklyTimeSpent = 0
                        )
                    )
                }
                navController.navigate("${NavigationItem.Statistics.route}/${topic.name}") {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route)
                    }
                }
            },
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium2))
                .align(Alignment.End),
            enabled = trackEntryUiState.isFormValid
        ) {
            Icon(
                Icons.Filled.Add,
                null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun EditField(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    leadingIcon: ImageVector,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    isError: Boolean
) {
    val borderColor = if (isError) {
        Color.Red
    } else {
        Color.Transparent // Or your default border color
    }
    TextField(
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

@Composable
fun DatePickerFieldToModal(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    onValueChanged: (String) -> Unit,
    isStartDatePicker: Boolean,
    startingDate: String,
    isError: Boolean
) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }
    var selectedDateString by remember { mutableStateOf("") }

    val borderColor = if (isError) {
        Color.Red
    } else {
        Color.Transparent
    }

    TextField(
        value = selectedDateString,
        onValueChange = {
            selectedDateString = it
            onValueChanged(it)
        },
        label = { Text(stringResource(label)) },
        leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Select date") },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            },
        shape = MaterialTheme.shapes.large,
        colors = TextFieldDefaults.colors(
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = borderColor,
            unfocusedIndicatorColor = borderColor
        )
    )

    if (showModal) {
        if (isStartDatePicker) { // Use the parameter to decide which modal to show
            StartDatePickerModal(
                onDateSelected = { millis ->
                    selectedDate = millis
                    selectedDateString = millis?.let { convertMillisToDate(it) } ?: ""
                    onValueChanged(selectedDateString)
                },
                onDismiss = { showModal = false }
            )
        } else {
            EndDatePickerModal(
                onDateSelected = { millis ->
                    selectedDate = millis
                    selectedDateString = millis?.let { convertMillisToDate(it) } ?: ""
                    onValueChanged(selectedDateString)
                },
                onDismiss = { showModal = false },
                startingDate = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).parse(startingDate)?.time ?: 0
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartDatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val today = Calendar.getInstance().timeInMillis
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = today
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            dateValidator = { timestamp ->
                timestamp >= today
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndDatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    startingDate: Long
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = startingDate
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            dateValidator = { timestamp ->
                timestamp > startingDate
            }
        )
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
