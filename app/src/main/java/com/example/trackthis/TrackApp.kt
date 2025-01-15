package com.example.trackthis

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.trackthis.ui.navigation.Navigation
import com.example.trackthis.ui.home.BottomBar
import com.example.trackthis.ui.home.TopAppBar
import com.example.trackthis.ui.insert_track.TrackEntryViewModel
import com.example.trackthis.ui.statistics.timer.TimerViewModel

/**
 * The main composable function for the Track_This application.
 *
 * This function sets up the primary UI structure, including the top app bar,
 * bottom navigation bar, and the main navigation component. It also initializes
 * and provides access to the necessary ViewModels for managing application state.
 *
 * The function uses a [Scaffold] to provide a basic screen layout, with a
 * [TopAppBar] at the top, a [BottomBar] at the bottom, and a [Navigation]
 * composable in the main content area.
 *
 * The function also retrieves the list of tracked topics from the [TrackEntryViewModel]
 * and passes it to the [BottomBar] and [Navigation] composable.
 *
 * @see TopAppBar
 * @see BottomBar
 * @see Navigation
 * @see TimerViewModel
 * @see TrackEntryViewModel
 */

@Composable
fun TrackApp() {
    val navController = rememberNavController()
    val timerViewModel: TimerViewModel = viewModel(factory = TimerViewModel.factory)
    val trackEntryViewModel: TrackEntryViewModel = viewModel(factory = TrackEntryViewModel.factory)
    val trackedTopics by trackEntryViewModel.retrieveAllItemsByUserId().collectAsState(emptyList())

    Scaffold(
        topBar = { TopAppBar(navController = navController) },
        bottomBar = { BottomBar(navController = navController, trackedTopics = trackedTopics, timerViewModel = timerViewModel) },
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
    ) { innerPadding ->
        Navigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            timerViewModel = timerViewModel,
            trackedTopics = trackedTopics
        )
    }
}