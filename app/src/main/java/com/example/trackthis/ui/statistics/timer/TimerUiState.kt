package com.example.trackthis.ui.statistics.timer

data class TimerUiState(
    val timer: Long = 0,
    val isPaused: Boolean = false,
    val isTimerRunning: Boolean = false
)