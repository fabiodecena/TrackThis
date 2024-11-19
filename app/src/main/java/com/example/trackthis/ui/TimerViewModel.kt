package com.example.trackthis.ui


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackthis.component.charts.ChartUiState
import com.example.trackthis.component.charts.defaultPointsData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale
import java.time.format.TextStyle


class TimerViewModel : ViewModel() {
    private val _timer = MutableStateFlow(0L)
    val timer = _timer.asStateFlow()

    private val _chartUiState = MutableStateFlow(ChartUiState())
    val chartUiState = _chartUiState.asStateFlow()

    init {
        _chartUiState.value.defaultPointsData.toMutableList()
    }

    private var timerJob: Job? = null

    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _timer.value++
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
    }

    fun stopTimer() {
        val currentDay = saveCurrentDay()
        val index = getIndexForDay(currentDay)
        val currentValue = timer.value
        updatePointsDataList(index, currentValue)
        _timer.value = 0
        timerJob?.cancel()
    }

    private fun saveCurrentDay(): String {
        val currentDate = LocalDate.now(ZoneId.systemDefault())
        val dayOfWeek = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return dayOfWeek
    }
    fun updatePointsDataList(index: Int, value: Long) {
        Log.d("ChartViewModel", "before change: ${_chartUiState.value.defaultPointsData.joinToString()}")
        var updatedList = _chartUiState.value.defaultPointsData.toMutableList()
        updatedList = defaultPointsData
        updatedList[index] = value.toDouble()
        _chartUiState.update { currentState ->
            currentState.copy(defaultPointsData = updatedList)
        }
        Log.d("ChartViewModel", "after change: ${_chartUiState.value.defaultPointsData.joinToString()}")
    }

    fun getIndexForDay(day: String): Int {
        return chartUiState.value.yLabels.indexOf(day)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
fun Long.formatTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val remainingSeconds = this % 60
    return String.format(locale = Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, remainingSeconds)
}