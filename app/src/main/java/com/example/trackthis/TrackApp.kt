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

@Composable
fun TrackApp() {
    val navController = rememberNavController()
    val timerViewModel: TimerViewModel = viewModel(factory = TimerViewModel.factory)
    val trackEntryViewModel: TrackEntryViewModel = viewModel(factory = TrackEntryViewModel.factory)
    val trackedTopics by trackEntryViewModel.retrieveAllItems().collectAsState(emptyList())

    Scaffold(
        topBar = { TopAppBar(navController = navController) },
        bottomBar = { BottomBar(navController = navController, trackedTopics = trackedTopics) },
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