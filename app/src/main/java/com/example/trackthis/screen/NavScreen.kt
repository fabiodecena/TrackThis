package com.example.trackthis.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.trackthis.R
import com.example.trackthis.component.BuildTracking
import com.example.trackthis.component.StartedTopic
import com.example.trackthis.component.TopicCard
import com.example.trackthis.component.TopicListItem
import com.example.trackthis.component.bars.BottomBar
import com.example.trackthis.component.bars.TopAppBar
import com.example.trackthis.data.StartedTopicElement
import com.example.trackthis.data.listOfStartedTopic
import com.example.trackthis.data.listOfVisualizedTopicListItem
import com.example.trackthis.data.pointsData
import com.example.trackthis.data.trackNavigationItems
import com.example.trackthis.navigation.Navigation
import com.example.trackthis.navigation.NavigationSelectionScreen
import com.example.trackthis.ui.AppViewModel
import com.example.trackthis.ui.TimerScreen
import com.example.trackthis.ui.TimerViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopAppBar(navController = navController) },
        bottomBar = { BottomBar(navController = navController) },
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
    ) { innerPadding ->
        Navigation(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun SettingsScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopRowSelectionScreen(navController = navController) },
    ) { innerPadding ->
        NavigationSelectionScreen(
            navController = navController, modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun TopRowSelectionScreen(modifier: Modifier = Modifier, navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        trackNavigationItems.forEach { item ->
            Text(
                modifier = Modifier
                    .clickable {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    .background(
                        if (currentRoute == item.route) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp),
                text = item.title
            )
        }
    }
}

@Composable
fun ActiveTrackScreen() {
    LazyColumn(modifier = Modifier) {
        items(listOfVisualizedTopicListItem.filter { it.selected }) { topic ->
            TopicListItem(topic)
        }
    }
}

@Composable
fun InactiveTrackScreen() {
    LazyColumn(modifier = Modifier) {
        items(listOfVisualizedTopicListItem.filter { !it.selected }) { topic ->
            TopicListItem(topic)
        }
    }
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.black))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Profile View",
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel = viewModel(),
    navController: NavController
) {
    val appUiState by appViewModel.appUiState.collectAsState()

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_small)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        items(appViewModel.updateTopicList()) { it ->
            TopicCard(
                navController = navController,
                topic = it,
                appUiState = appUiState,
                onCardButtonClick = { appViewModel.toggleExpanded(it) },
            )
        }
    }
}

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    topics: List<StartedTopicElement>
) {
    val topicList = remember { mutableStateListOf<StartedTopicElement>().apply { addAll(topics) } }

    LazyColumn(modifier = modifier) {
        items(topicList) { topic ->
            StartedTopic(
                topicElement = topic,
                onDelete = { topicList.remove(topic) },
                pointsData = pointsData
            )
        }
    }
}

@Composable
fun BuildScreen(
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel
) {
    val timerValue by timerViewModel.timer.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(listOfStartedTopic) { topic ->
            BuildTracking(
                timerViewModel = timerViewModel,
                topicElement = topic
            )
        }
    }

    TimerScreen(
        timerValue = timerValue,
        onStartClick = { timerViewModel.startTimer() },
        onPauseClick = { timerViewModel.pauseTimer() },
        onStopClick = { timerViewModel.stopTimer() }
    )
}
