package com.example.trackthis.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.trackthis.Navigation
import com.example.trackthis.R
import com.example.trackthis.TopicCard
import com.example.trackthis.TopicListItem
import com.example.trackthis.bars.BottomBar
import com.example.trackthis.bars.TopAppBar
import com.example.trackthis.data.listOfVisualizedTopicListItem
import com.example.trackthis.data.listOfVisualizedTopics
import com.example.trackthis.data.trackNavigationItems
import com.example.trackthis.visualizeTopics

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopAppBar(navController = navController) },
        bottomBar = { BottomBar(navController = navController) }
    ) { innerPadding ->
        Navigation(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}
@Composable
fun SettingsScreen(modifier: Modifier = Modifier, navController: NavController) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = modifier
                .padding(10.dp)
        ) {
            Text(
                modifier = modifier
                    .clickable { navController.navigate(trackNavigationItems[0].route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true }
                    }
                    .padding(10.dp),
                text = "Active Tracking")
            Text(
                modifier = modifier
                    .clickable { navController.navigate(trackNavigationItems[1].route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true }
                    }
                    .padding(10.dp),
                text = "Inactive Tracking")
        }
        LazyColumn(
            modifier = modifier
                .height(450.dp)
        ) {
            items(listOfVisualizedTopicListItem.filter { it.selected }) { topic ->
                TopicListItem(topic)
            }
        }
    }
}

@Composable
fun ActiveTrackScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = modifier
                .padding(10.dp),
            text = "Active Tracking")
        LazyColumn(
            modifier = modifier
                .height(450.dp)
        ) {
            items(listOfVisualizedTopicListItem.filter { it.selected }) { topic ->
                TopicListItem(topic)
            }
        }
    }
}
@Composable
fun InactiveTrackScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = modifier
                .padding(10.dp),
            text = "Inactive Tracking")
        LazyColumn(
            modifier = modifier
                .height(450.dp)
        ) {
            items(listOfVisualizedTopicListItem.filter { !it.selected }) { topic ->
                TopicListItem(topic)
            }
        }
    }
}
@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
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
fun HomeScreen( modifier: Modifier = Modifier) {
    val visualizedTopics = visualizeTopics(
        listOfVisualizedTopic = listOfVisualizedTopics,
        listOfVisualizedTopicListItem = listOfVisualizedTopicListItem
    )
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_small)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        items(visualizedTopics) { topic ->
            TopicCard(topic)
        }
    }
}
@Composable
fun BuildScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.light_grey))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Profile View",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}
@Composable
fun LocationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.purple_500))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Profile View",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}