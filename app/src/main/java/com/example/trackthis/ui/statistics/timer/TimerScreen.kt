package com.example.trackthis.ui.statistics.timer

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.time.delay
import java.time.Duration
import java.time.LocalDateTime

@Composable
fun TimerScreen(
    timerViewModel: TimerViewModel,
    navController: NavController,
    topicId: Int
) {
    val timerValue by timerViewModel.timer.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScheduleDailyTimerReset(timerViewModel)
        Text(text = timerValue.formatTime(), fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { timerViewModel.pauseTimer() }) {
                Text("Pause")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { timerViewModel.stopTimer(context, navController, topicId) },
                enabled = timerValue > 0L
            ) {
                Text("Stop")
            }
        }
    }
}
@Composable
fun ScheduleDailyTimerReset(viewModel: TimerViewModel) {
    val currentContext = LocalContext.current

    // Launch a coroutine to handle resetting
    LaunchedEffect(Unit) {
        while (true) {
            // Calculate the time until midnight
            val now = LocalDateTime.now()
            val midnight = now.toLocalDate().plusDays(1).atStartOfDay()
            val durationUntilMidnight = Duration.between(now, midnight)

            // Delay until midnight
            delay(durationUntilMidnight)

            // Reset the timer at midnight
            viewModel.resetTimer()

            // Optionally notify the user or log reset
            Toast.makeText(currentContext, "Timer reset for the new day!", Toast.LENGTH_SHORT).show()
        }
    }
}
