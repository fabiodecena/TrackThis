package com.example.trackthis.ui.history

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackthis.R
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.ui.insert_track.TrackEntryViewModel
import com.example.trackthis.ui.navigation.NavigationItem
import com.example.trackthis.ui.statistics.timer.TimerViewModel
import kotlinx.coroutines.launch

/**
 * [HistoryScreen] is a composable function that displays a list of tracked topics with their progress.
 *
 * @param modifier Modifier for the layout.
 * @param trackEntryViewModel ViewModel for managing tracked topic entries and operations.
 * @param timerViewModel ViewModel for managing the timer state and other related data.
 * @param trackedTopics List of [TrackedTopic] to display.
 * @param navigateOnSelectedClick Lambda to execute when a tracked topic is selected.
 * @param navController Navigation controller for navigating between screens.
 */
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    trackEntryViewModel: TrackEntryViewModel,
    timerViewModel: TimerViewModel,
    trackedTopics: List<TrackedTopic>,
    navigateOnSelectedClick: (Int) -> Unit,
    navController: NavController
) {
    val timerUiState by timerViewModel.timerUiState.collectAsState()
    val currentContext = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
       items(
           items = trackedTopics,
           key = { trackedTopic -> trackedTopic.id }
       ) { trackedTopic ->
           HistoryElement(
               trackedTopic = trackedTopic,
               trackEntryViewModel = trackEntryViewModel,
               timerViewModel = timerViewModel,
               modifier = Modifier.clickable {
                   if (timerUiState.isTimerRunning) {
                       navController.navigate(NavigationItem.History.route)
                       Toast.makeText(
                           currentContext,
                           R.string.timer_running_toast, Toast.LENGTH_SHORT
                       ).show()
                   } else {
                       timerViewModel.initializeTimer(trackedTopic)
                       navigateOnSelectedClick(trackedTopic.name)
                   }
               }
           )
       }
    }
}
/**
 * [HistoryElement] is a composable function that displays a single tracked topic's details and progress.
 *
 * @param modifier Modifier for the layout.
 * @param trackedTopic The [TrackedTopic] to display.
 * @param trackEntryViewModel ViewModel for managing tracked topic entries.
 * @param timerViewModel ViewModel for managing the timer state.
 */
@Composable
fun HistoryElement(
    modifier: Modifier = Modifier,
    trackedTopic: TrackedTopic,
    trackEntryViewModel: TrackEntryViewModel,
    timerViewModel: TimerViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val score = (trackedTopic.totalTimeSpent) * 100 / trackedTopic.finalGoal

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_small)),
        shape = MaterialTheme.shapes.large
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                            .padding(top= dimensionResource(R.dimen.padding_small)),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    text = stringResource(trackedTopic.name)
                )
                Button(
                    colors = buttonColors(colorResource(R.color.light_red)),
                    onClick = {
                        timerViewModel.pauseTimer()
                        timerViewModel.initializeTimer(trackedTopic)
                        coroutineScope.launch {
                            trackEntryViewModel.deleteItem(trackedTopic)
                        }
                    }
                ) {
                    Text(stringResource(R.string.delete_button))
                }
            }
            Column(
                modifier = Modifier
                    .padding(end = dimensionResource(R.dimen.padding_medium2)),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = AnnotatedString.Builder().apply {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(R.string.starting_date_label))
                        }
                            append(trackedTopic.startingDate)
                    }.toAnnotatedString()
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                Text(
                    text = AnnotatedString.Builder().apply {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(R.string.ending_date_label))
                        }
                            append(trackedTopic.endingDate)
                    }.toAnnotatedString()
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.padding_small)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.progress_bar) + ": " + (score).toString() + "%",
                style = MaterialTheme.typography.bodySmall
            )
        }
        ShowProgress(score)
    }
}
/**
 * [ShowProgress] is a composable function that displays a progress bar based on the given score.
 * The [score] parameter represents the progress of the user based on the [TrackedTopic.totalTimeSpent] for a specific [TrackedTopic]
 * and the [TrackedTopic.finalGoal] set by the user.
 *
 * @param score The progress score (0-100) to display.
 */
@Composable
fun ShowProgress(score : Int){
    val gradient = Brush.linearGradient(listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary)
    )
    val progressFactor by remember(score) { mutableFloatStateOf(score*0.01f) }

    Row(
        modifier = Modifier
            .padding(
                bottom = dimensionResource(R.dimen.padding_medium),
                start = dimensionResource(R.dimen.padding_medium),
                end = dimensionResource(R.dimen.padding_medium)
            )
            .fillMaxWidth()
            .height(45.dp)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                ),
                shape = RoundedCornerShape(50.dp)
            )
            .clip(
                RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomEndPercent = 50,
                    bottomStartPercent = 50
                )
            )
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (score > 0) {
            Button(
                contentPadding = PaddingValues(1.dp),
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth(progressFactor)
                    .background(brush = gradient),
                enabled = false,
                elevation = null,
                colors = buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            ) {

            }
        }
    }
}

