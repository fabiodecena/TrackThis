package com.example.trackthis.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = MaterialTheme.colorScheme.onErrorContainer,
            containerColor = MaterialTheme.colorScheme.secondary
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
                Box(
                    modifier = Modifier
                        .size(48.dp) // Adjust size for the background
                        .background(
                            color = if (currentRoute == NavigationItem.Settings.route) {
                                MaterialTheme.colorScheme.secondaryContainer // Selected background
                            } else {
                                Color.Transparent // Default background
                            },
                            shape = CircleShape // Circle background
                        ),
                    contentAlignment = Alignment.Center // Center the icon
                ) {
                    Icon(
                        imageVector = NavigationItem.Settings.icon!!,
                        contentDescription = NavigationItem.Settings.title,
                        tint = if (currentRoute == NavigationItem.Settings.route) {
                            MaterialTheme.colorScheme.inverseOnSurface // Selected tint
                        } else {
                            MaterialTheme.colorScheme.onSecondaryContainer // Default tint
                        }
                    )
                }
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
                Box(
                    modifier = Modifier
                        .size(48.dp) // Adjust size for the background
                        .background(
                            color = if (currentRoute == NavigationItem.Welcome.route ||
                                currentRoute == NavigationItem.Registration.route ||
                                currentRoute == NavigationItem.Login.route) {
                                MaterialTheme.colorScheme.secondaryContainer // Selected background
                            } else {
                                Color.Transparent // Default background
                            },
                            shape = CircleShape // Circle background
                        ),
                    contentAlignment = Alignment.Center // Center the icon
                ) {
                    Icon(
                        imageVector = NavigationItem.Registration.icon!!,
                        contentDescription = NavigationItem.Registration.title,
                        tint = if (currentRoute == NavigationItem.Welcome.route ||
                            currentRoute == NavigationItem.Registration.route ||
                            currentRoute == NavigationItem.Login.route) {
                            MaterialTheme.colorScheme.inverseOnSurface // Selected tint
                        } else {
                            MaterialTheme.colorScheme.onSecondaryContainer // Default tint
                        }
                    )
                }
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
    val timerUiState by timerViewModel.timerUiState.collectAsState()
    val topic = timerUiState.topic
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
                        if (item.route == NavigationItem.Statistics.route) {
                            currentRoute?.startsWith(item.route) == true
                        } else {
                            currentRoute == item.route
                        }
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
                            if (!timerUiState.isTimerRunning) {
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