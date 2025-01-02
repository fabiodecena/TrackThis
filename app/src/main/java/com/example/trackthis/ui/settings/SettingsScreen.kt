package com.example.trackthis.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.trackthis.R
import com.example.trackthis.data.TopicListElement
import com.example.trackthis.ui.navigation.trackNavigationItems
import com.example.trackthis.ui.home.HomeScreenViewModel
import com.example.trackthis.ui.navigation.NavigationSelectionScreen

@Composable
fun SettingsScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopRowSelectionScreen(navController = navController) },
        containerColor = MaterialTheme.colorScheme.inverseOnSurface
    ) { innerPadding ->
        NavigationSelectionScreen(
            navController = navController,
            modifier = Modifier
                .padding(innerPadding)
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
fun ActiveTrackScreen(viewModel: HomeScreenViewModel) {
    val topics by viewModel.topics.collectAsState()

    LazyColumn(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
        items(topics.filter { it.selected }) { topic ->
            TopicListItem(topic) {
                viewModel.onTopicSelected(it)
            }
        }
    }
}

@Composable
fun InactiveTrackScreen(viewModel: HomeScreenViewModel) {
    val topics by viewModel.topics.collectAsState()

    LazyColumn(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
        items(topics.filter { !it.selected }) { topic ->
            TopicListItem(topic) {
                viewModel.onTopicSelected(it)
            }
        }
    }
}


@Composable
fun TopicListItem(topicListItem: TopicListElement, onClick: (TopicListElement) -> Unit) {
    val checked by remember { mutableStateOf(topicListItem.selected) }
    Surface(
        color = MaterialTheme.colorScheme.surface,
        onClick = {
            onClick(topicListItem)
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            colors = if(checked) CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
            else CardDefaults.cardColors(MaterialTheme.colorScheme.onTertiary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = topicListItem.imageRes),
                    contentDescription = stringResource(topicListItem.name),
                    modifier = Modifier,

                    contentScale = ContentScale.Fit
                )
                Text(
                    text = LocalContext.current.getString(topicListItem.name),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}