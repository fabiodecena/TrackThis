package com.example.trackthis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.trackthis.component.TrackDetails
import com.example.trackthis.data.NavigationItem
import com.example.trackthis.data.Topic
import com.example.trackthis.data.TopicListElement
import com.example.trackthis.data.listOfVisualizedTopics
import com.example.trackthis.screen.ActiveTrackScreen
import com.example.trackthis.screen.BuildScreen
import com.example.trackthis.screen.HomeScreen
import com.example.trackthis.screen.InactiveTrackScreen
import com.example.trackthis.screen.LocationScreen
import com.example.trackthis.screen.MainScreen
import com.example.trackthis.screen.ProfileScreen
import com.example.trackthis.screen.SettingsScreen
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


