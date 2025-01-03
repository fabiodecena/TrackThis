package com.example.trackthis.ui.statistics.timer

import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import kotlinx.coroutines.Job

/**
 * [TimerUiState] represents the UI state for a timer.
 *
 * @property timer The current value of the timer in milliseconds.
 * @property timerJob The [kotlinx.coroutines.Job] associated with the timer coroutine.
 * It's used to control the timer (start, pause, cancel).
 * @property isPaused Indicates whether the timer is currently paused.
 * @property isTimerRunning Indicates whether the timer is currently running.
 * @property topic The [TrackedTopic] associated with this timer.
 */
data class TimerUiState(
    val timer: Long = 0,
    val timerJob: Job? = null,
    val isPaused: Boolean = false,
    val isTimerRunning: Boolean = false,
    val topic: TrackedTopic? = null
)