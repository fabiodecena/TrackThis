package com.example.trackthis.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.trackthis.R
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.ui.insert_track.TrackEntryViewModel
import com.example.trackthis.ui.statistics.timer.TimerViewModel
import kotlinx.coroutines.launch


@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    trackEntryViewModel: TrackEntryViewModel,
    timerViewModel: TimerViewModel
) {
    val trackedTopics by trackEntryViewModel.retrieveAllItems().collectAsState(emptyList())

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
               timerViewModel = timerViewModel
           )
       }
    }
}

@Composable
fun HistoryElement(
    modifier: Modifier = Modifier,
    trackedTopic: TrackedTopic,
    trackEntryViewModel: TrackEntryViewModel,
    timerViewModel: TimerViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_small))
    ){
        Text("Name: ${stringResource(trackedTopic.name)}")
        Text("Total Time Spent: ${trackedTopic.timeSpent}")
        Text("Daily Effort: ${trackedTopic.dailyEffort}")
        Text("Final Goal: ${trackedTopic.finalGoal}")
        Text("Starting Date: ${trackedTopic.startingDate}")
        Text("Ending Date: ${trackedTopic.endingDate}")
        Text("Index: ${trackedTopic.index}")
        Text("Daily Time Spent: ${trackedTopic.dailyTimeSpent}")

        Button(
            onClick = {
                timerViewModel.resetData()
                coroutineScope.launch {
                trackEntryViewModel.deleteItem(trackedTopic)
                }
            }
        ){
            Text("Delete")
        }
        ShowProgress((trackedTopic.timeSpent)*100/trackedTopic.finalGoal)
    }
}

@Composable
fun ShowProgress(score : Int){
    val gradient = Brush.linearGradient(listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary)
    )
    val progressFactor by remember(score) { mutableFloatStateOf(score*0.01f) }

    Row(
        modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth().height(45.dp).border(
            width = 4.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary
                )
            ) ,
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
                Text(text = (score).toString()+"%",
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(23.dp))
                        .fillMaxHeight(0.87f)
                        .fillMaxWidth()
                        .padding(7.dp),
                    color = Color.White,
                    textAlign = TextAlign.Center)
            }
        }
    }
}

