package com.example.trackthis.ui.insert_track

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.trackthis.TrackApplication
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.data.database.tracked_topic.TrackedTopicDao
import com.example.trackthis.ui.statistics.timer.TimerUiState
import com.example.trackthis.ui.statistics.timer.TimerViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel to validate and insert and manage items in the Room database.
 *
 * @param trackedTopicDao The Data Access Object for the TrackedTopic entity.
 */
class TrackEntryViewModel(private val trackedTopicDao: TrackedTopicDao) : ViewModel() {

    private val _trackEntryUiState = MutableStateFlow(TrackEntryUiState())
    /**
     *  StateFlow to manage the UI state of the track entry screen.
     */
    val timerViewModel = TimerViewModel
    val trackEntryUiState: StateFlow<TrackEntryUiState> = _trackEntryUiState.asStateFlow()

    var userId: String =
        FirebaseAuth.getInstance().currentUser?.uid ?: ""

    fun updateDailyEffort(dailyEffort: String) {
        _trackEntryUiState.value = _trackEntryUiState.value.copy(dailyEffort = dailyEffort)
        validateInputs()
    }
    fun updateFinalGoal(finalGoal: String) {
        _trackEntryUiState.value = _trackEntryUiState.value.copy(finalGoal = finalGoal)
        validateInputs()
    }

    fun updateStartingDate(startingDate: String) {
        _trackEntryUiState.value = _trackEntryUiState.value.copy(startingDate = startingDate)
        validateInputs()
    }
    fun updateEndingDate(endingDate: String) {
        _trackEntryUiState.value = _trackEntryUiState.value.copy(endingDate = endingDate)
        validateInputs()
    }
    /**
     * Validates the input fields in the UI state and updates the error flags and form validity.
     */
    private fun validateInputs() {
        val state = _trackEntryUiState.value

        val isDailyEffortGreaterThanFinalGoal =
            state.dailyEffort.isNotBlank() && state.finalGoal.isNotBlank() && (state.dailyEffort.toIntOrNull()
                ?: 0) >= (state.finalGoal.toIntOrNull() ?: 0)

        val isDailyEffortGreaterThan24 =
            state.dailyEffort.toInt() > 24

        val isStartingDateGreaterThanEndingDate =
            state.startingDate.isNotBlank() &&
                state.endingDate.isNotBlank() &&
                (SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).parse(state.startingDate)?.time ?: 0) >=
                SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).parse(state.endingDate)?.time!!

        val isFormValid =
                !isDailyEffortGreaterThanFinalGoal &&
                    !isStartingDateGreaterThanEndingDate &&
                    !isDailyEffortGreaterThan24 &&
                    state.dailyEffort.isNotBlank() &&
                    state.finalGoal.isNotBlank() &&
                    state.startingDate.isNotBlank() &&
                    state.endingDate.isNotBlank() &&
                    userId != "" &&
                        !TimerUiState().isTimerRunning


        _trackEntryUiState.value = state.copy(
            isDailyEffortErrorGreaterThanFinalGoal = isDailyEffortGreaterThanFinalGoal,
            isDailyEffortErrorGreaterThan24 = isDailyEffortGreaterThan24,
            isDateError = isStartingDateGreaterThanEndingDate,
            isFormValid = isFormValid
        )
    }

    /**
     * Converts milliseconds to a formatted date string to adjust the [DatePickerFieldToModal]
     *
     * @param millis The time in milliseconds.
     * @return The formatted date string.
     */
    fun convertMillisToDate(millis: Long): String {
        val formatter = android.icu.text.SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return formatter.format(Date(millis))
    }

    /**
     * Functions to update [TrackedTopic] in the Room database
     */
    suspend fun addNewItem(trackedTopic: TrackedTopic) {
        trackedTopicDao.insert(trackedTopic)
    }

    suspend fun deleteItem(trackedTopic: TrackedTopic) {
        trackedTopicDao.delete(trackedTopic)
    }

    fun retrieveAllItemsByUserId(): Flow<List<TrackedTopic>> {
        return  trackedTopicDao.getAllItemsByUser(userId)
    }
    /**
     * Factory for creating [TrackEntryViewModel] instances.
     */
    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as TrackApplication
                TrackEntryViewModel(application.database.trackedTopicDao())
            }
        }
    }
}


