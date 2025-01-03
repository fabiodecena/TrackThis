package com.example.trackthis

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.trackthis.data.database.tracked_topic.TrackedTopicDao
import com.example.trackthis.ui.statistics.timer.TimerViewModel
import kotlinx.coroutines.flow.first
import java.time.DayOfWeek
import java.time.LocalDate
import com.example.trackthis.data.database.tracked_topic.TrackedTopic

/**
 * [MondayResetWorker] is a [CoroutineWorker] that resets daily time spent for tracked topics.
 *
 * This worker is scheduled to run every day at midnight to reset the [TrackedTopic.dailyTimeSpent]
 * for each [TrackedTopic] at the beginning of the Week.
 * It also saves the [TrackedTopic.totalTimeSpent] for the past week and resets the timer daily.
 *
 * @param context The application context.
 * @param workerParams The worker parameters.
 */
class MondayResetWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val trackedTopicDao = (applicationContext as TrackApplication).database.trackedTopicDao()
        val timerViewModel = TimerViewModel(trackedTopicDao)

        // Perform Monday check
        if (LocalDate.now().dayOfWeek == DayOfWeek.MONDAY) {
            saveTotalTimeSpent(trackedTopicDao)
            resetDailyTimeSpentForTrackedTopics(trackedTopicDao)
            timerViewModel.resetData()
            timerViewModel.updatePointsDataList(topic = trackedTopicDao.getAllItems().first().first())
            timerViewModel.resetTimer()
        }
        // Reset timer daily
        timerViewModel.resetTimer()
        setProgress(workDataOf("status" to "done"))
        return Result.success()
    }
    /**
     * Saves the [TrackedTopic.totalTimeSpent] for each [TrackedTopic] to the [TrackedTopic.weeklyTimeSpent].
     *
     * @param trackedTopicDao The data access object for tracked topics.
     */
    private suspend fun saveTotalTimeSpent(trackedTopicDao: TrackedTopicDao) {
        val trackedTopics = trackedTopicDao.getAllItems().first()
        for (topic in trackedTopics) {
            val updatedTopic = topic.copy(weeklyTimeSpent = topic.totalTimeSpent)
            trackedTopicDao.update(updatedTopic)
        }
    }
    /**
     * Resets the [TrackedTopic.dailyTimeSpent] for each [TrackedTopic].
     *
     * This function clears the [TrackedTopic.dailyTimeSpent] map and updates the [TrackedTopic.totalTimeSpent]
     * by adding the [TrackedTopic.weeklyTimeSpent].
     * Thus, after the reset of the [TrackedTopic.dailyTimeSpent] the progress done by the user is not lost but saved into the [TrackedTopic.totalTimeSpent].
     *
     * @param trackedTopicDao The data access object for tracked topics.
     */
    private suspend fun resetDailyTimeSpentForTrackedTopics(trackedTopicDao: TrackedTopicDao) {
        val trackedTopics = trackedTopicDao.getAllItems().first()
        for (topic in trackedTopics) {
            val updatedDailyTimeSpent = topic.dailyTimeSpent.toMutableMap()
            updatedDailyTimeSpent.clear()
            val totalEffort = updatedDailyTimeSpent.values.sum()
            val updatedTopic = topic.copy(
                dailyTimeSpent = updatedDailyTimeSpent,
                totalTimeSpent = totalEffort.toInt() + topic.weeklyTimeSpent
            )
            trackedTopicDao.update(updatedTopic)
        }
    }
}