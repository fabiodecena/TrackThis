package com.example.trackthis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.trackthis.data.Topic
import com.example.trackthis.data.TopicListElement
import com.example.trackthis.ui.theme.TrackThisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackThisTheme {
                Surface(modifier = Modifier
                    .fillMaxSize()
                ) {
                    TrackApp()
                }
            }
        }
    }
}
fun visualizeTopics(
    listOfVisualizedTopicListItem: List<TopicListElement>
): List<TopicListElement> {
    // Create a map of visualized topics by name
    val visualizedTopicsListItemsMap = listOfVisualizedTopicListItem.associateBy { it.name }

    // Filter the list of visualized topic list items to only include those with matching names
    val matchingVisualizedTopic = listOfVisualizedTopicListItem.filter {

        visualizedTopicsListItemsMap[it.name]?.selected ?: true
    }
    return matchingVisualizedTopic
}


