package com.example.trackthis.ui.insert_track


data class TrackEntryUiState(
    val dailyEffort: String = "",
    val finalGoal: String = "",
    val startingDate: String = "",
    val endingDate: String = "",
    val selectedDate: Long? = null,
    val selectedDateString: String = "",
    val showModal: Boolean = false,
    val isDailyEffortError: Boolean = false,
    val isDateError: Boolean = false,
    val isFormValid: Boolean = false
)