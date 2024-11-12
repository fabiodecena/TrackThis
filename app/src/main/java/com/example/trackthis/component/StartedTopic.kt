package com.example.trackthis.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.trackthis.data.StartedTopicElement

@Composable
fun StartedTopic(
    topicElement: StartedTopicElement,
) {
    Column {
        Text(
            text = stringResource(id = topicElement.name)
        )
    }
}