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
import com.example.trackthis.data.TopicListElement
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.ui.navigation.NavigationItem
import com.example.trackthis.ui.statistics.timer.TimerViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import com.example.trackthis.ui.statistics.StatisticsScreen


/**
 * This composable provides a screen for users to input details related to a specific [topic] they want to track.
 * It includes fields for [TrackedTopic.dailyEffort], [TrackedTopic.finalGoal], [TrackedTopic.startingDate], and [TrackedTopic.endingDate].
 * It also handles the submission of this data to the [TrackEntryViewModel] and navigates to the [StatisticsScreen]
 *
 * @param modifier Modifier for styling the layout.
 * @param topic The [TopicListElement] for which details are being entered.
 * @param navController The NavController for navigation.
 * @param timerViewModel The [TimerViewModel] for managing timer data and operations.
 * @param trackEntryViewModel The [TrackEntryViewModel] for managing track entry data.
 */
@Composable
fun TrackEntryScreen(
    modifier: Modifier = Modifier,
    topic: TopicListElement,
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
            trackEntryViewModel = trackEntryViewModel,
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
            trackEntryViewModel = trackEntryViewModel,
            label = R.string.ending_date,
            onValueChanged = { trackEntryViewModel.updateEndingDate(it) },
            isStartDatePicker = false,
            startingDate = trackEntryUiState.startingDate,
            isError = trackEntryUiState.isDateError
        )
        Button(
            onClick = {
                timerViewModel.resetTimer()
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
/**
 * This composable provides a styled text field with a leading icon, used for data input.
 *
 * @param modifier Modifier for styling the layout.
 * @param label The string resource for the text field label.
 * @param leadingIcon The ImageVector for the leading icon.
 * @param keyboardOptions The keyboard options for the text field.
 * @param value The current value of the text field.
 * @param onValueChanged Callback for when the text field value changes.
 * @param isError Boolean indicating if the text field has an error.
 */
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
        Color.Transparent
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
/**
 * This composable provides a text field that, when clicked, opens a date picker modal for selecting a date.
 *
 * The date picker modal is displayed as a dialog, allowing the user to select a date.
 * The selected date is then formatted and displayed in the text field.
 *
 * @param modifier Modifier for styling the layout.
 * @param trackEntryViewModel The [TrackEntryViewModel] for managing track entry data.
 * @param label The string resource for the text field label.
 * @param onValueChanged Callback for when the date is selected. It provides the selected date as a formatted string.
 * @param isStartDatePicker Boolean indicating if this is for the start date picker.
 *  If true, the date picker will only allow dates from today onwards.
 *  If false, it will use the [startingDate] to validate the selected date.
 * @param startingDate The current starting date, used to validate the selected date in the end date picker.
 * @param isError Boolean indicating if the date field has an error.
 *  If true, the text field's border will be displayed in red.
 */
@Composable
fun DatePickerFieldToModal(
    modifier: Modifier = Modifier,
    trackEntryViewModel: TrackEntryViewModel,
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
        if (isStartDatePicker) {
            StartDatePickerModal(
                onDateSelected = { millis ->
                    selectedDate = millis
                    selectedDateString = millis?.let { trackEntryViewModel.convertMillisToDate(it) } ?: ""
                    onValueChanged(selectedDateString)
                },
                onDismiss = { showModal = false }
            )
        } else {
            EndDatePickerModal(
                onDateSelected = { millis ->
                    selectedDate = millis
                    selectedDateString = millis?.let { trackEntryViewModel.convertMillisToDate(it) } ?: ""
                    onValueChanged(selectedDateString)
                },
                onDismiss = { showModal = false },
                startingDate = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).parse(startingDate)?.time ?: 0
            )
        }
    }
}
/**
 *  This composable displays a date picker dialog that allows the user to select a date.
 *  The selected date must be equal to or after the current date.
 *
 *  @param onDateSelected Callback for when a date is selected. It provides the selected date in milliseconds.
 *  @param onDismiss Callback for when the dialog is dismissed.
 */
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
/**
 * This composable displays a date picker dialog that allows the user to select a date.
 * The selected date must be after the [startingDate].
 *
 * @param onDateSelected Callback for when a date is selected. It provides the selected date in milliseconds.
 * @param onDismiss Callback for when the dialog is dismissed.
 * @param startingDate The starting date in milliseconds. The selected date must be after this date.
 */
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

