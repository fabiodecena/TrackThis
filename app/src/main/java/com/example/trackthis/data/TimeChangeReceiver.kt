package com.example.trackthis.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.trackthis.ui.statistics.timer.WorkerScheduler.scheduleMondayResetWorker

class TimeChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_TIME_CHANGED || intent?.action == Intent.ACTION_TIMEZONE_CHANGED) {
            // Reschedule the worker when the time or timezone changes
            Log.d("TimeChangeReceiver", "Time or timezone changed. Rescheduling worker.")
            scheduleMondayResetWorker(context)
        }
    }
}