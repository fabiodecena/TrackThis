package com.example.trackthis.ui.insert_track


data class TrackEntryUiState(
    val dailyEffort: String = "",
    val finalGoal: String = "",
    val startingDate: String = "",
    val endingDate: String = "",
    val isDailyEffortError: Boolean = false,
    val isDateError: Boolean = false,
    val isFormValid: Boolean = false
)