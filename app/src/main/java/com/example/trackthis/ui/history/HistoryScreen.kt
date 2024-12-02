package com.example.trackthis.ui.history

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.trackthis.R
import com.example.trackthis.data.database.TrackedTopic
import com.example.trackthis.ui.insert_track.TrackEntryViewModel
import kotlinx.coroutines.launch


@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    trackEntryViewModel: TrackEntryViewModel
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
           HistoryElement(trackedTopic = trackedTopic, trackEntryViewModel = trackEntryViewModel)
       }
    }
}


@Composable
fun HistoryElement(
    modifier: Modifier = Modifier,
    trackedTopic: TrackedTopic,
    trackEntryViewModel: TrackEntryViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_small))
    ){
        Text("Name: ${trackedTopic.name}")
        Text("Total Time Spent: ${trackedTopic.timeSpent}")
        Text("Starting Date: ${trackedTopic.startingDate}")
        Text("Ending Date: ${trackedTopic.endingDate}")
        Button(
            onClick = {
                coroutineScope.launch {
                trackEntryViewModel.deleteItem(trackedTopic)
                }
            }
        ){
            Text("Delete")
        }
    }
}

