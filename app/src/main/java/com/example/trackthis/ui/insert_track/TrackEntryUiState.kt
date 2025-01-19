package com.example.trackthis.ui.insert_track

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Data class that represents the current UI state in TrackEntryViewModel, which is reflected in the [TrackEntryScreen]
 *
 * @property dailyEffort The user's daily effort input.
 * @property finalGoal The user's final goal input.
 * @property startingDate The starting date of the tracking period.
 * @property endingDate The ending date of the tracking period.
 * @property selectedDate The selected date as a timestamp.
 * @property selectedDateString The selected date as a formatted string.
 * @property showModal Controls the visibility of a modal dialog.
 * @property isDailyEffortErrorGreaterThanFinalGoal Indicates if there's an error with the daily effort input.
 * @property isDateError Indicates if there's an error with the selected date.
 * @property isFormValid Indicates if the form is valid and can be submitted.
 */
data class TrackEntryUiState(
    val dailyEffort: String = "",
    val finalGoal: String = "",
    val startingDate: String = "",
    val endingDate: String = "",
    val selectedDate: Long? = null,
    val selectedDateString: String = "",
    val showModal: Boolean = false,
    val isDailyEffortErrorGreaterThanFinalGoal: Boolean = false,
    val isDailyEffortErrorGreaterThan24: Boolean = false,
    val isDateError: Boolean = false,
    val isFormValid: Boolean = false
)
