package com.example.trackthis.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.trackthis.data.Topic
import com.example.trackthis.data.listOfVisualizedTopics


@Composable
fun TrackDetails(modifier: Modifier, topic: Topic = findExpandedTopicName(listOfVisualizedTopics)
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(topic.name))
        topic.expanded = false
    }

}