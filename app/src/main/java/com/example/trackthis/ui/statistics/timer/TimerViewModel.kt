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
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.trackthis.TrackApplication
import com.example.trackthis.data.MondayResetWorker
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.data.database.tracked_topic.TrackedTopicDao
import com.example.trackthis.ui.navigation.NavigationItem
import com.example.trackthis.ui.statistics.charts.ChartUiState
import com.example.trackthis.ui.statistics.charts.pointsData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale
import java.util.concurrent.TimeUnit


class TimerViewModel(private val trackedTopicDao: TrackedTopicDao) : ViewModel() {

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as TrackApplication
                TimerViewModel(application.database.trackedTopicDao())
            }
        }
    }

    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    private val _timerUiState = MutableStateFlow(TimerUiState())
    val timerUiState = _timerUiState.asStateFlow()

    private val _chartUiState = MutableStateFlow(ChartUiState())
    private val chartUiState = _chartUiState.asStateFlow()

    private var timerJob: Job? = null

    private val _topic = MutableStateFlow<TrackedTopic?>(null)
    val topic = _topic.asStateFlow()

    fun setTopic(topic: TrackedTopic?) {
        _topic.value = topic
    }

    fun startTimer() {
        _timerUiState.value = _timerUiState.value.copy(isTimerRunning = true)
        if (timerJob == null || timerJob?.isCancelled == true)
            _timerUiState.value = _timerUiState.value.copy(isPaused = false)
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _timerUiState.value=_timerUiState.value.copy(timer = _timerUiState.value.timer + 1)
            }
        }
    }

    fun scheduleMondayResetWorker(context: Context) {
        WorkerScheduler.scheduleMondayResetWorker(context)
    }

    fun observeMondayResetWorker(context: Context, navController: NavController, firstTopic: TrackedTopic?) {
        WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkLiveData("MondayResetWorker")
            .observe( navController.currentBackStackEntry!!) { workInfos ->
                workInfos?.forEach { workInfo ->
                    val progress = workInfo.progress.getString("status")
                    if (progress != "done" && workInfo.state == WorkInfo.State.RUNNING) {
                        resetTimer() // Reset the timer or update your state
                        navController.navigate("${NavigationItem.Statistics.route}/${firstTopic?.name}") {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) { inclusive = true }
                            }
                        }
                    }
                }
            }
    }

    fun initializeTimer(topic: TrackedTopic?) {
        if (timerJob == null || timerJob?.isCancelled == true) {
            val currentDay = saveCurrentDay()
            val savedTime = topic?.dailyTimeSpent?.get(currentDay) ?: 0L
            _timerUiState.value = _timerUiState.value.copy(timer = savedTime)
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _timerUiState.value = _timerUiState.value.copy(isPaused = true)
    }

    fun stopTimer(context: Context, navController: NavController, topicId: Int) {
        _timerUiState.value = _timerUiState.value.copy(isPaused = true)
        val currentDay = saveCurrentDay()
        timerJob?.cancel()
       _timerUiState.value = _timerUiState.value.copy(isTimerRunning = false)

        // History the alert dialog
        _timerUiState.value = _timerUiState.value.copy(isPaused = true)// Pause the timer during the alert dialog
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm Update")
        builder.setMessage("Are you sure you want to update the data list?")

        builder.setPositiveButton("Yes") { dialog, _ ->

            dialog.dismiss()

            viewModelScope.launch {
                val topic = trackedTopicDao.getItemByName(userId!!,topicId).first()
                val updatedDailyTimeSpent = topic.dailyTimeSpent.toMutableMap()
                updatedDailyTimeSpent[currentDay] = timerUiState.value.timer // Store time for current day
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
            _timerUiState.value = _timerUiState.value.copy(isPaused = false)
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
        _timerUiState.value = _timerUiState.value.copy(timer = 0L)
        timerJob?.cancel()
        _timerUiState.value = _timerUiState.value.copy(timer = 0L)
        _timerUiState.value = _timerUiState.value.copy(isPaused = false)
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

object WorkerScheduler {
    fun scheduleMondayResetWorker(context: Context) {
        val initialDelay = calculateInitialDelayToMidnight()

        WorkManager.getInstance(context).cancelUniqueWork("MondayResetWorker")

        val workRequest = PeriodicWorkRequestBuilder<MondayResetWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(false)
                    .setRequiresDeviceIdle(false)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "MondayResetWorker",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            workRequest
        )
    }

    private fun calculateInitialDelayToMidnight(): Long {
        val now = LocalDateTime.now(ZoneId.systemDefault())
        val nextMonday = now.plusDays(1)
            .withHour(0).withMinute(0).withSecond(0).withNano(0)
        return Duration.between(now, nextMonday).toMillis()
    }
}