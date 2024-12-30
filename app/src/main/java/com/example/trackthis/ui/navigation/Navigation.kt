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
import com.example.trackthis.ui.insert_track.TrackDetails
import com.example.trackthis.ui.statistics.charts.ChartViewModel
import com.example.trackthis.data.TopicListRepository
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.data.listOfVisualizedTopics
import com.example.trackthis.ui.history.HistoryScreen
import com.example.trackthis.ui.home.HomeScreen
import com.example.trackthis.ui.home.HomeScreenViewModel
import com.example.trackthis.ui.insert_track.TrackEntryViewModel
import com.example.trackthis.ui.profile.LoginScreen
import com.example.trackthis.ui.profile.LoginViewModel
import com.example.trackthis.ui.profile.RegistrationScreen
import com.example.trackthis.ui.profile.RegistrationViewModel
import com.example.trackthis.ui.profile.WelcomeScreen
import com.example.trackthis.ui.settings.ActiveTrackScreen
import com.example.trackthis.ui.settings.InactiveTrackScreen
import com.example.trackthis.ui.settings.SettingsScreen
import com.example.trackthis.ui.statistics.StatisticsScreen
import com.example.trackthis.ui.statistics.timer.TimerViewModel

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
    val loginViewModel: LoginViewModel = viewModel()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavigationItem.Welcome.route
    ){
        composable(NavigationItem.Welcome.route) {
            WelcomeScreen(
                navController = navController,
                registrationViewModel = registrationViewModel,
                loginViewModel = loginViewModel
            )
        }
        composable(NavigationItem.Registration.route) {
            RegistrationScreen(navController = navController)
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
                    navController = navController,
                    firstTopic = null
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
                        chartViewModel = chartViewModel, timerViewModel = timerViewModel,
                        navController = navController,
                        firstTopic = topic
                    )
                }
            }
        composable(NavigationItem.History.route) {
            HistoryScreen(
                trackEntryViewModel = trackEntryViewModel, timerViewModel = timerViewModel,
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
