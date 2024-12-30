package com.example.trackthis.ui.insert_track

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.trackthis.TrackApplication
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.data.database.tracked_topic.TrackedTopicDao
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Locale


/**
 * ViewModel to validate and insert items in the Room database.
 */
class TrackEntryViewModel(private val trackedTopicDao: TrackedTopicDao) : ViewModel() {

    private val _trackEntryUiState = MutableStateFlow(TrackEntryUiState())
    val trackEntryUiState: StateFlow<TrackEntryUiState> = _trackEntryUiState.asStateFlow()


    fun updateSelectedDate(millis: Long?) {
        _trackEntryUiState.update { currentState ->
            currentState.copy(
                selectedDate = millis,
                selectedDateString = millis?.let { convertMillisToDate(it) } ?: ""
            )
        }
    }

    fun toggleModal(show: Boolean) {
        _trackEntryUiState.update { currentState ->
            currentState.copy(showModal = show)
        }
    }



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

    private fun validateInputs() {
        val state = _trackEntryUiState.value

        val isDailyEffortGreaterThanFinalGoal =
            state.dailyEffort.isNotBlank() && state.finalGoal.isNotBlank() && (state.dailyEffort.toIntOrNull()
                ?: 0) >= (state.finalGoal.toIntOrNull() ?: 0)

        val isStartingDateGreaterThanEndingDate =
            state.startingDate.isNotBlank() &&
                state.endingDate.isNotBlank() &&
                (SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).parse(state.startingDate)?.time ?: 0) >=
                SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).parse(state.endingDate)?.time!!

        val isFormValid =
            !isDailyEffortGreaterThanFinalGoal &&
                !isStartingDateGreaterThanEndingDate &&
                state.dailyEffort.isNotBlank() &&
                state.finalGoal.isNotBlank() &&
                state.startingDate.isNotBlank() &&
                state.endingDate.isNotBlank()

        _trackEntryUiState.value = state.copy(
            isDailyEffortError = isDailyEffortGreaterThanFinalGoal,
            isDateError = isStartingDateGreaterThanEndingDate,
            isFormValid = isFormValid
        )
    }

    /**
     * Functions to update [TrackedTopic] in the Room database
     */
    private val userId: String =
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseAuth.getInstance().currentUser!!.uid
        } else {
              ""
        }

    suspend fun addNewItem(trackedTopic: TrackedTopic) {
        trackedTopicDao.insert(trackedTopic)
    }

    suspend fun deleteItem(trackedTopic: TrackedTopic) {
        trackedTopicDao.delete(trackedTopic)
    }

    fun retrieveAllItems(): Flow<List<TrackedTopic>> {
        return  trackedTopicDao.getAllItemsByUser(userId)
    }

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as TrackApplication
                TrackEntryViewModel(application.database.trackedTopicDao())
            }
        }
    }
}


