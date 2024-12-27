package com.example.trackthis.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.trackthis.TrackApplication
import com.example.trackthis.data.database.tracked_topic.TrackedTopicDao
import com.example.trackthis.ui.statistics.timer.TimerViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import java.time.DayOfWeek
import java.time.LocalDate

class MondayResetWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    override suspend fun doWork(): Result {
        val trackedTopicDao = (applicationContext as TrackApplication).database.trackedTopicDao()
        val timerViewModel = TimerViewModel(trackedTopicDao)

        // Perform Monday check
        if (LocalDate.now().dayOfWeek == DayOfWeek.MONDAY) {
            saveTotalTimeSpent(trackedTopicDao)
            resetDailyTimeSpentForTrackedTopics(trackedTopicDao)
            timerViewModel.resetData()
            timerViewModel.updatePointsDataList(firstTopic = trackedTopicDao.getAllItems(userId!!).first().first())
            timerViewModel.resetTimer()
        }
        timerViewModel.resetTimer()
        setProgress(workDataOf("status" to "done"))
        // Indicate whether the work finished successfully with Result.success()
        return Result.success()
    }

    private suspend fun saveTotalTimeSpent(trackedTopicDao: TrackedTopicDao) {
        val trackedTopics = trackedTopicDao.getAllItems(userId!!).first()
        for (topic in trackedTopics) {
            val updatedTopic = topic.copy(index = topic.timeSpent)
            trackedTopicDao.update(updatedTopic)
        }
    }

    private suspend fun resetDailyTimeSpentForTrackedTopics(trackedTopicDao: TrackedTopicDao) {
        val trackedTopics = trackedTopicDao.getAllItems(userId!!).first()
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