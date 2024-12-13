package com.example.trackthis.ui.statistics.timer

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.example.trackthis.TrackApplication
import com.example.trackthis.data.NavigationItem
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.data.database.tracked_topic.TrackedTopicDao
import com.example.trackthis.ui.statistics.charts.ChartUiState
import com.example.trackthis.ui.statistics.charts.pointsData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale



class TimerViewModel(private val trackedTopicDao: TrackedTopicDao) : ViewModel() {

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as TrackApplication
                TimerViewModel(application.database.trackedTopicDao())
            }
        }
    }

    private val _timer = MutableStateFlow(0L)
    val timer = _timer.asStateFlow()

    private val _chartUiState = MutableStateFlow(ChartUiState())
    private val chartUiState = _chartUiState.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused = _isPaused.asStateFlow()

    private var timerJob: Job? = null

    fun startTimer() {
        if (timerJob == null || timerJob?.isCancelled == true)
            _isPaused.value = false
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _timer.value++
            }
        }
    }

    init {
        // Initialize and start a coroutine to check for Monday and reset data
        viewModelScope.launch {
            while (true) {
                // Check if it's Monday
                if (LocalDate.now().dayOfWeek == DayOfWeek.MONDAY) {
                    saveTotalTimeSpent()
                    resetDailyTimeSpentForTrackedTopics()
                }
                // Delay for 24 hours
                delay(24 * 60 * 60 * 1000)
            }
        }
    }
    private suspend fun saveTotalTimeSpent() {
        val trackedTopics = trackedTopicDao.getAllItems().first()
        for (topic in trackedTopics) {
            val updatedTopic = topic.copy(index = topic.timeSpent)
            trackedTopicDao.update(updatedTopic)
        }
    }
    private suspend fun resetDailyTimeSpentForTrackedTopics() {
        val trackedTopics = trackedTopicDao.getAllItems().first()
        for (topic in trackedTopics) {
            val updatedDailyTimeSpent = topic.dailyTimeSpent.toMutableMap()
            updatedDailyTimeSpent.clear()
            val totalEffort = updatedDailyTimeSpent.values.sum()// Update total time spent
            val updatedTopic = topic.copy(dailyTimeSpent = updatedDailyTimeSpent, timeSpent = totalEffort.toInt() + topic.index)
            trackedTopicDao.update(updatedTopic)
        }
       resetData()
    }


    fun initializeTimer(topic: TrackedTopic?) {
        if (timerJob == null || timerJob?.isCancelled == true) {
            val currentDay = saveCurrentDay()
            val savedTime = topic?.dailyTimeSpent?.get(currentDay) ?: 0L
            _timer.value = savedTime
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _isPaused.value = true
    }

    fun stopTimer(context: Context, navController: NavController, topicId: Int) {
        val currentDay = saveCurrentDay()
        timerJob?.cancel()
        _isPaused.value = false

        // Build the alert dialog
        _isPaused.value = true// Pause the timer during the alert dialog
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm Update")
        builder.setMessage("Are you sure you want to update the data list?")

        builder.setPositiveButton("Yes") { dialog, _ ->

            dialog.dismiss()

            viewModelScope.launch {
                val topic = trackedTopicDao.getItemByName(topicId).first()
                val updatedDailyTimeSpent = topic.dailyTimeSpent.toMutableMap()
                updatedDailyTimeSpent[currentDay] = timer.value // Store time for current day
                val totalEffort = updatedDailyTimeSpent.values.sum() + topic.index // Update total time spent
                val updatedTopic = topic.copy(dailyTimeSpent = updatedDailyTimeSpent, timeSpent = totalEffort.toInt())
                trackedTopicDao.update(updatedTopic)

                // Update chart data using accumulated time for all days
                val updatedPointsDataList = mutableListOf<Double>()
                chartUiState.value.xLabels.forEach { day ->
                    updatedPointsDataList.add(updatedDailyTimeSpent[day]?.toDouble() ?: 0.0)
                }
                _chartUiState.update { currentState ->
                    currentState.copy(defaultPointsData = updatedPointsDataList)
                }
                resetTimer()
                navController.navigate("${NavigationItem.Statistics.route}/${updatedTopic.name}") {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route)
                    }
                }
                return@launch
            }
        }
        builder.setNegativeButton("No") { dialog, _ ->
            // Resume the timer
            _isPaused.value = false
            startTimer() // Restart the timer when the user chooses "No"
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
    fun updatePointsDataList(firstTopic: TrackedTopic?) {
        chartUiState.value.xLabels.forEachIndexed { index, day -> // Get the timeSpent for the current day
            val timeSpent = firstTopic?.dailyTimeSpent?.get(day)
            if (timeSpent != null) { // Only update if the value exists in the map
                if (index < pointsData.size) {
                    pointsData[index] = timeSpent.toDouble()
                }
            } else {
                pointsData[index] = 0.0
            }
        }
    }

    private fun saveCurrentDay(): String {
        val currentDate = LocalDate.now(ZoneId.systemDefault())
        val dayOfWeek = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return dayOfWeek
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    fun resetTimer() {
        timerJob?.cancel()
        _timer.value = 0L
        _isPaused.value = false
    }

    fun resetData() {
        val updatedList: MutableList<Double> = pointsData
        updatedList.indices.forEach { index ->
            updatedList[index] = 0.0
        }
        _chartUiState.update { currentState ->
            currentState.copy(defaultPointsData = updatedList)
        }
    }
}

fun Long.formatTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val remainingSeconds = this % 60
    return String.format(locale = Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, remainingSeconds)
}

