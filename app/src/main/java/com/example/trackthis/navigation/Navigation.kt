package com.example.trackthis.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.trackthis.component.TrackDetails
import com.example.trackthis.data.NavigationItem
import com.example.trackthis.data.listOfVisualizedTopics
import com.example.trackthis.screen.ActiveTrackScreen
import com.example.trackthis.screen.StatisticsScreen
import com.example.trackthis.screen.HomeScreen
import com.example.trackthis.screen.InactiveTrackScreen
import com.example.trackthis.screen.LocationScreen
import com.example.trackthis.screen.ProfileScreen
import com.example.trackthis.screen.SettingsScreen

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
            route = "${NavigationItem.TrackDetails.route}/{topicId}",
            arguments = listOf(navArgument("topicId") { type = NavType.IntType })
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getInt("topicId")
            val topic = listOfVisualizedTopics.find { it.name == topicId }
            topic?.let {
                TrackDetails(topic = topic, navController = navController)
            }
        }
        composable(NavigationItem.Statistics.route) {
            StatisticsScreen()
        }
        composable(NavigationItem.Location.route) {
            LocationScreen()
        }
        composable(NavigationItem.Settings.route) {
            SettingsScreen()
        }
        composable(NavigationItem.Profile.route) {
            ProfileScreen()
        }
    }
}

@Composable
fun NavigationSelectionScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavigationItem.ActiveTrackSelection.route
    ){
        composable(NavigationItem.ActiveTrackSelection.route) {
            ActiveTrackScreen()
        }
        composable(NavigationItem.InactiveTrackSelection.route) {
            InactiveTrackScreen()
        }
    }
}
