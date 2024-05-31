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
import com.example.trackthis.data.trackDetails
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
@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavigationItem.Home.route
    ){
        composable(NavigationItem.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(
            route = "${trackDetails[0].route}/{topicId}",
            arguments = listOf(navArgument("topicId") { type = NavType.IntType })
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getInt("topicId")
            val topic = listOfVisualizedTopics.find { it.name == topicId }
            topic?.let {
                TrackDetails(topic = topic)
            }
        }
        composable(NavigationItem.Build.route) {
            BuildScreen()
        }
        composable(NavigationItem.Location.route) {
            LocationScreen()
        }
        composable(NavigationItem.Settings.route) {
            SettingsScreen(navController = navController)
        }
        composable(NavigationItem.ActiveTrackSelection.route) {
            ActiveTrackScreen(navController = navController)
        }
        composable(NavigationItem.InactiveTrackSelection.route) {
            InactiveTrackScreen(navController = navController)
        }
        composable(NavigationItem.Profile.route) {
            ProfileScreen()
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


