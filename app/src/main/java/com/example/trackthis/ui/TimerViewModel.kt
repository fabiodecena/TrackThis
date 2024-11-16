package com.example.trackthis.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackthis.component.charts.ChartViewModel
import com.example.trackthis.data.pointsData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale
import java.time.format.TextStyle

class TimerViewModel : ViewModel() {
    private val _timer = MutableStateFlow(0L)
    val timer = _timer.asStateFlow()

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

    fun stopTimer(viewModel: ChartViewModel): Int {
        val currentDay = saveCurrentDay()
        val index = viewModel.getIndexForDay(currentDay)
        val currentValue = timer.value
        if(index in pointsData.indices) {
            pointsData[index] = pointsData[index].copy(y = currentValue.toFloat())
        }

        timerJob?.cancel()
        return index

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
}
fun Long.formatTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val remainingSeconds = this % 60
    return String.format(locale = Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, remainingSeconds)
}