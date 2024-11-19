package com.example.trackthis.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.trackthis.R
import com.example.trackthis.data.StartedTopicElement
import com.example.trackthis.ui.TimerViewModel


@Composable
fun BuildTracking(
    topicElement: StartedTopicElement,
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel
) {
    val timerValue = timerViewModel.timer.collectAsState().value
    val isPaused = timerViewModel.isPaused.collectAsState().value
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = modifier
                .padding(dimensionResource(R.dimen.padding_medium))
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Row(
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.padding_medium))
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    enabled = timerValue == 0L || isPaused,
                    onClick = { timerViewModel.startTimer() },
                    modifier = Modifier
                        .padding(start = dimensionResource(R.dimen.padding_medium))
                ) {
                    Icon(
                        painterResource(R.drawable.play_circle_24dp_fill0_wght400_grad0_opsz24),
                        contentDescription = null,
                        tint = if (timerValue == 0L || isPaused) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onTertiary // Optional: Show a disabled color
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(end = dimensionResource(R.dimen.padding_medium2)),
                    text = stringResource(topicElement.name),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}
