package com.example.trackthis.ui.history

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.trackthis.R
import com.example.trackthis.data.listOfStartedTopic
import com.example.trackthis.ui.statistics.timer.TimerViewModel

@Composable
fun History(
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(listOfStartedTopic) { topic ->

        }
    }
}