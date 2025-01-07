package com.example.trackthis

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimeChangeReceiverTest {

    private var workerCalled = false

    private val testScheduleWorker: (Context) -> Unit = {
        workerCalled = true
    }
    private val context: Context = ApplicationProvider.getApplicationContext()
    @Test
    fun onReceive_should_call_scheduleWorker_on_ACTION_TIME_CHANGED() {
        // Arrange
        val intent = Intent(Intent.ACTION_TIME_CHANGED)
        val receiver = TimeChangeReceiver(testScheduleWorker)

        // Act
        receiver.onReceive(context, intent)

        // Assert
        assertTrue("scheduleWorker should be called", workerCalled)
    }

    @Test
    fun onReceive_should_call_scheduleWorker_on_ACTION_TIMEZONE_CHANGED() {
        // Arrange
        val intent = Intent(Intent.ACTION_TIMEZONE_CHANGED)
        val receiver = TimeChangeReceiver(testScheduleWorker)

        // Act
        receiver.onReceive(context, intent)

        // Assert
        assertTrue("scheduleWorker should be called", workerCalled)
    }

    @Test
    fun onReceive_should_not_call_scheduleWorker_for_other_actions() {
        // Arrange
        val intent = Intent(Intent.ACTION_CAMERA_BUTTON)// Other action
        val receiver = TimeChangeReceiver(testScheduleWorker)

        // Act
        receiver.onReceive(context, intent)

        // Assert
        assertTrue("scheduleWorker should not be called", !workerCalled)
    }
}
