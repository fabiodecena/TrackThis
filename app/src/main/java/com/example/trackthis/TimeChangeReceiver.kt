package com.example.trackthis

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.trackthis.ui.statistics.timer.WorkerScheduler.scheduleMondayResetWorker

/**
 * [TimeChangeReceiver] is a [BroadcastReceiver] that listens for time changes and timezone changes.
 *
 * This receiver is triggered when the system time or timezone is changed. Upon receiving such an event,
 * it reschedules the [scheduleMondayResetWorker] to ensure that the worker is executed at the correct time
 * based on the new time or timezone.
 *
 * The receiver is registered to listen for the following intents:
 * - [Intent.ACTION_TIME_CHANGED]: Triggered when the system time is changed.
 * - [Intent.ACTION_TIMEZONE_CHANGED]: Triggered when the system timezone is changed.
 *
 * @see scheduleMondayResetWorker
 */
class TimeChangeReceiver(
    private val scheduleWorker: (Context) -> Unit = ::scheduleMondayResetWorker
) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_TIME_CHANGED || intent?.action == Intent.ACTION_TIMEZONE_CHANGED) {
            scheduleWorker(context)
        }
    }
}