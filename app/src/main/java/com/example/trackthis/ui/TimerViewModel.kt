package com.example.trackthis.ui


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackthis.component.charts.ChartUiState
import com.example.trackthis.component.charts.pointsData
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

    init {
        _chartUiState.value.defaultPointsData.toMutableList()
    }

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

    fun stopTimer() {
        Log.d("Before press Stop", "stopTimer: ${_chartUiState.value.defaultPointsData}")
        val currentDay = saveCurrentDay()
        val index = getIndexForDay(currentDay)
        val currentValue = timer.value
        updatePointsDataList(index, currentValue)
        Log.d("After press Stop", "stopTimer: ${_chartUiState.value.defaultPointsData}")
        timerJob?.cancel()
        _isPaused.value = false
        _timer.value = 0

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