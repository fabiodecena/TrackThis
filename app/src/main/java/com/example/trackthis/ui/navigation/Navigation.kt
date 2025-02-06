package com.example.trackthis.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.trackthis.data.TopicListRepository
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.data.listOfVisualizedTopicListItem
import com.example.trackthis.ui.history.HistoryScreen
import com.example.trackthis.ui.home.HomeScreen
import com.example.trackthis.ui.home.HomeScreenViewModel
import com.example.trackthis.ui.insert_track.TrackEntryScreen
import com.example.trackthis.ui.insert_track.TrackEntryViewModel
import com.example.trackthis.ui.profile.LoginScreen
import com.example.trackthis.ui.profile.RegistrationScreen
import com.example.trackthis.ui.profile.RegistrationViewModel
import com.example.trackthis.ui.profile.WelcomeScreen
import com.example.trackthis.ui.settings.ActiveTrackScreen
import com.example.trackthis.ui.settings.InactiveTrackScreen
import com.example.trackthis.ui.settings.SettingsScreen
import com.example.trackthis.ui.statistics.StatisticsScreen
import com.example.trackthis.ui.statistics.charts.ChartViewModel
import com.example.trackthis.ui.statistics.timer.TimerViewModel

/**
 * This composable sets up the navigation graph using Jetpack Navigation Compose.
 * It defines the different screens of the application and how to navigate between them.
 *
 * @param modifier Modifier for the NavHost.
 * @param navController NavHostController for managing navigation.
 * @param timerViewModel ViewModel for managing timer-related data.
 * @param trackedTopics List of tracked topics to display.
 * @param trackEntryViewModel ViewModel for managing track entry data.
 */
@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    timerViewModel: TimerViewModel,
    trackedTopics: List<TrackedTopic>,
    trackEntryViewModel: TrackEntryViewModel = viewModel(factory = TrackEntryViewModel.factory)
) {
    val chartViewModel: ChartViewModel = viewModel()
    val registrationViewModel: RegistrationViewModel = viewModel()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavigationItem.Welcome.route
    ){
        composable(NavigationItem.Welcome.route) {
            WelcomeScreen(
                navController = navController,
                registrationViewModel = registrationViewModel
            )
        }
        composable(NavigationItem.Registration.route) {
            RegistrationScreen(registrationViewModel = registrationViewModel)
        }
        composable(NavigationItem.Login.route) {
            LoginScreen()
        }
        composable(NavigationItem.Home.route) {
            HomeScreen(navController = navController, trackedTopics = trackedTopics)
        }
        composable(
            route = "${NavigationItem.TrackDetails.route}/{topicId}",
            arguments = listOf(navArgument("topicId") { type = NavType.IntType })
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getInt("topicId")
            val topic = listOfVisualizedTopicListItem.find { it.name == topicId }
            topic?.let {
                TrackEntryScreen(
                    topic = topic,
                    navController = navController,
                    timerViewModel = timerViewModel,
                    trackEntryViewModel = trackEntryViewModel
                )
            }
        }
        composable(NavigationItem.Statistics.route) {
            StatisticsScreen(
                chartViewModel = chartViewModel,
                timerViewModel = timerViewModel,
                navController = navController,
                topic = null
            )
        }
        composable(
            route = "${NavigationItem.Statistics.route}/{topicId}",
            arguments = listOf(navArgument("topicId") { type = NavType.IntType })
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getInt("topicId")
            val topic = trackedTopics.find { it.name == topicId }
            timerViewModel.setTopic(topic)
            topic?.let {
                StatisticsScreen(
                    chartViewModel = chartViewModel,
                    timerViewModel = timerViewModel,
                    navController = navController,
                    topic = topic
                )
            }
        }
        composable(NavigationItem.History.route) {
            HistoryScreen(
                trackEntryViewModel = trackEntryViewModel,
                timerViewModel = timerViewModel,
                trackedTopics = trackedTopics,
                navigateOnSelectedClick = { topicId ->
                    navController.navigate("${NavigationItem.Statistics.route}/$topicId")
                },
                navController = navController
            )
        }
        composable(NavigationItem.Settings.route) {
            SettingsScreen()
        }
    }
}
/**
 * This composable sets up the navigation graph for the active and inactive track selection screens.
 *
 * @param navController NavHostController for managing navigation.
 * @param modifier Modifier for the NavHost.
 */
@Composable
fun NavigationSelectionScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel = viewModel { HomeScreenViewModel(TopicListRepository(context)) }
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavigationItem.ActiveTrackSelection.route
    ){
        composable(NavigationItem.ActiveTrackSelection.route) {
            ActiveTrackScreen(viewModel)
        }
        composable(NavigationItem.InactiveTrackSelection.route) {
            InactiveTrackScreen(viewModel)
        }
    }
}
