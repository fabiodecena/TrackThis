package com.example.trackthis.ui.home

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.trackthis.R
import com.example.trackthis.ui.navigation.NavigationItem
import com.example.trackthis.ui.navigation.bottomBarNavigationItems
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.ui.statistics.timer.TimerViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(navController: NavController) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = MaterialTheme.colorScheme.onErrorContainer,
            containerColor = MaterialTheme.colorScheme.secondary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        title = {
            Text(
                stringResource(R.string.topbar_title),
                fontFamily = FontFamily.Cursive,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigate(NavigationItem.Settings.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = NavigationItem.Settings.icon!!,
                    contentDescription = NavigationItem.Settings.title
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    navController.navigate(NavigationItem.Welcome.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = NavigationItem.Registration.icon!!,
                    contentDescription = NavigationItem.Registration.title
                )
            }
        },
    )
}

@Composable
fun BottomBar(
    navController: NavController,
    trackedTopics: List<TrackedTopic>,
    timerViewModel: TimerViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isTimerRunning by timerViewModel.isTimerRunning.collectAsState(initial = false)
    val topic by timerViewModel.topic.collectAsState()
    val firstTopicName = trackedTopics.firstOrNull()?.name

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.secondary
    ) {
        bottomBarNavigationItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon!!, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected =
                    if (item.route == NavigationItem.Home.route || item.route == NavigationItem.History.route) {
                        currentRoute == item.route
                    } else {
                        currentRoute == "${NavigationItem.Statistics.route}/{topicId}"
                    },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.inverseOnSurface,
                    selectedTextColor = MaterialTheme.colorScheme.inverseOnSurface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unselectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                onClick = {
                    if (item.route == NavigationItem.Home.route || item.route == NavigationItem.History.route) {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route)
                            }
                        }
                    } else {
                        if (trackedTopics.isEmpty()) {
                            navController.navigate(item.route) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route)
                                }
                            }
                        } else {
                            if (!isTimerRunning) {
                                timerViewModel.initializeTimer(topic)
                            }

                            val topicName = topic?.name
                            if (topicName != null) {
                                navController.navigate("${NavigationItem.Statistics.route}/$topicName") {
                                    navController.graph.startDestinationRoute?.let { route ->
                                        popUpTo(route)
                                    }
                                }
                            } else {
                                navController.navigate("${NavigationItem.Statistics.route}/$firstTopicName") {
                                    navController.graph.startDestinationRoute?.let { route ->
                                        popUpTo(route)
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}