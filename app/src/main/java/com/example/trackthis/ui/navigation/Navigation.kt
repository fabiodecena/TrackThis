package com.example.trackthis.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.trackthis.ui.insert_track.TrackDetails
import com.example.trackthis.ui.statistics.charts.ChartViewModel
import com.example.trackthis.data.NavigationItem
import com.example.trackthis.data.listOfVisualizedTopics
import com.example.trackthis.ui.history.HistoryScreen
import com.example.trackthis.ui.home.HomeScreen
import com.example.trackthis.ui.insert_track.TrackEntryViewModel
import com.example.trackthis.ui.profile.ProfileScreen
import com.example.trackthis.ui.settings.ActiveTrackScreen
import com.example.trackthis.ui.settings.InactiveTrackScreen
import com.example.trackthis.ui.settings.SettingsScreen
import com.example.trackthis.ui.statistics.StatisticsScreen
import com.example.trackthis.ui.statistics.timer.TimerViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    timerViewModel: TimerViewModel,
    trackEntryViewModel: TrackEntryViewModel = viewModel(factory = TrackEntryViewModel.factory),
    modifier: Modifier = Modifier
) {
    val chartViewModel: ChartViewModel = viewModel()

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
                TrackDetails(
                    topic = topic, navController = navController,
                    timerViewModel = timerViewModel, trackEntryViewModel = trackEntryViewModel
                )
            }
        }
        composable(NavigationItem.Statistics.route) {
            StatisticsScreen(
                chartViewModel = chartViewModel, timerViewModel = timerViewModel,
                navController = navController, trackEntryViewModel = trackEntryViewModel
            )
        }
        composable(NavigationItem.Build.route) {
            HistoryScreen(trackEntryViewModel = trackEntryViewModel)
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
