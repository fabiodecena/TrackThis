package com.example.trackthis.ui.statistics.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackthis.R
import com.example.trackthis.data.NavigationItem
import kotlinx.coroutines.launch

@Composable
fun TimerScreen(
    timerViewModel: TimerViewModel,
    navController: NavController,
    topicId: Int
) {
    val timerValue by timerViewModel.timer.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                colors = ButtonDefaults.buttonColors(colorResource(R.color.light_blue)),
                onClick = {
                    coroutineScope.launch {
                        timerViewModel.resetTimer()
                        timerViewModel.resetData()
                        navController.navigate("${NavigationItem.Statistics.route}/${topicId}") {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .wrapContentSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Repeat,
                        contentDescription = "Refresh Data"
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
                    Text(
                        text = stringResource(R.string.refresh),
                        maxLines = 1,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier.wrapContentWidth()
                    )
                }
            }
        }
    }
}


