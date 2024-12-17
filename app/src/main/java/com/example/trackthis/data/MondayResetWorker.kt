package com.example.trackthis.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.trackthis.TrackApplication
import com.example.trackthis.data.database.tracked_topic.TrackedTopicDao
import com.example.trackthis.ui.statistics.timer.TimerViewModel
import kotlinx.coroutines.flow.first
import java.time.DayOfWeek
import java.time.LocalDate

class MondayResetWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val trackedTopicDao = (applicationContext as TrackApplication).database.trackedTopicDao()

        // Perform Monday check
        if (LocalDate.now().dayOfWeek == DayOfWeek.MONDAY) {
            saveTotalTimeSpent(trackedTopicDao)
            resetDailyTimeSpentForTrackedTopics(trackedTopicDao)
        }

        // Indicate whether the work finished successfully with Result.success()
        return Result.success()
    }

    private suspend fun saveTotalTimeSpent(trackedTopicDao: TrackedTopicDao) {
        val trackedTopics = trackedTopicDao.getAllItems().first()
        for (topic in trackedTopics) {
            val updatedTopic = topic.copy(index = topic.timeSpent)
            trackedTopicDao.update(updatedTopic)
        }
    }

    private suspend fun resetDailyTimeSpentForTrackedTopics(trackedTopicDao: TrackedTopicDao) {
        val trackedTopics = trackedTopicDao.getAllItems().first()
        for (topic in trackedTopics) {
            val updatedDailyTimeSpent = topic.dailyTimeSpent.toMutableMap()
            updatedDailyTimeSpent.clear()
            val totalEffort = updatedDailyTimeSpent.values.sum()
            val updatedTopic = topic.copy(
                dailyTimeSpent = updatedDailyTimeSpent,
                timeSpent = totalEffort.toInt() + topic.index
            )
            trackedTopicDao.update(updatedTopic)
        }
    }
}