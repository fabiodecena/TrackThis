package com.example.trackthis.ui

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.trackthis.component.charts.ChartUiState
import com.example.trackthis.component.charts.pointsData
import com.example.trackthis.data.NavigationItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale


class TimerViewModel : ViewModel() {
    private val _timer = MutableStateFlow(0L)
    val timer = _timer.asStateFlow()

    private val _chartUiState = MutableStateFlow(ChartUiState())
    val chartUiState = _chartUiState.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused = _isPaused.asStateFlow()

    var timerJob: Job? = null

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

    fun pauseTimer() {
        timerJob?.cancel()
        _isPaused.value = true
    }

    fun stopTimer(context: Context, navController: NavController) {
        Log.d("Before press Stop", "stopTimer: ${_chartUiState.value.defaultPointsData}")
        val currentDay = saveCurrentDay()
        val index = getIndexForDay(currentDay)
        val currentValue = timer.value
        timerJob?.cancel()
        _isPaused.value = false

        // Build the alert dialog
        _isPaused.value = true// Pause the timer during the alert dialog
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm Update")
        builder.setMessage("Are you sure you want to update the data list?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            updatePointsDataList(index, currentValue)
            Log.d("After press Stop", "stopTimer: ${_chartUiState.value.defaultPointsData}")
            if (currentDay != saveCurrentDay()) {
                _timer.value = 0// Reset the timer
            }


            dialog.dismiss()

            navController.navigate(NavigationItem.Statistics.route) {
                navController.graph.startDestinationRoute?.let { route ->
                    popUpTo(route)
                }
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

    private fun saveCurrentDay(): String {
        val currentDate = LocalDate.now(ZoneId.systemDefault())
        val dayOfWeek = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return dayOfWeek
    }
    fun updatePointsDataList(index: Int, value: Long) {
        var updatedList = _chartUiState.value.defaultPointsData.toMutableList()
        updatedList = pointsData
        updatedList[index] = value.toDouble()
        _chartUiState.update { currentState ->
            currentState.copy(defaultPointsData = updatedList)
        }
    }

    fun getIndexForDay(day: String): Int {
        return chartUiState.value.yLabels.indexOf(day)
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
        var updatedList = _chartUiState.value.defaultPointsData.toMutableList()
        updatedList = pointsData
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