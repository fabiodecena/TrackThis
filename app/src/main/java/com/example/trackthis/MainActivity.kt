package com.example.trackthis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.trackthis.data.Topic
import com.example.trackthis.data.TopicListElement
import com.example.trackthis.screen.MainScreen
import com.example.trackthis.ui.theme.TrackThisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrackThisTheme {
                Surface(modifier = Modifier
                    .fillMaxSize()
                ) {
                    MainScreen()
                }
            }
        }
    }
}
fun visualizeTopics(
    listOfVisualizedTopic: List<Topic>,
    listOfVisualizedTopicListItem: List<TopicListElement>
): List<Topic> {
    // Create a map of visualized topics by name
    val visualizedTopicsListItemsMap = listOfVisualizedTopicListItem.associateBy { it.name }

    // Filter the list of visualized topic list items to only include those with matching names
    val matchingVisualizedTopic = listOfVisualizedTopic.filter {
        visualizedTopicsListItemsMap.containsKey(it.name)
        visualizedTopicsListItemsMap[it.name]?.selected ?: true
    }
    return matchingVisualizedTopic
}


