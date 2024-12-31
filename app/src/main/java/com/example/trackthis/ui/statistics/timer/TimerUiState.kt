package com.example.trackthis.ui.statistics.timer

import kotlinx.coroutines.Job

data class TimerUiState(
    val timer: Long = 0,
    val timerJob: Job? = null,
    val isPaused: Boolean = false,
    val isTimerRunning: Boolean = false
)