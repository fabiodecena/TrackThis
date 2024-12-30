package com.example.trackthis.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.trackthis.R
import com.example.trackthis.data.TopicListElement
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.ui.navigation.NavigationItem

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel = viewModel( factory = HomeScreenViewModel.factory),
    navController: NavController,
    trackedTopics: List<TrackedTopic>
) {
    val topics by homeScreenViewModel.topics.collectAsState() // Observe topics dynamically

    LaunchedEffect(topics) {
        homeScreenViewModel.updateTopicList()
    }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_small)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        items(homeScreenViewModel.updateTopicList()) { topic -> // Use the dynamic list here
            TopicCard(
                navController = navController,
                topic = topic,
                homeScreenUiState = homeScreenViewModel.homeScreenUiState.collectAsState().value,
                onCardButtonClick = { homeScreenViewModel.toggleExpanded(topic.name) },
                trackedTopics = trackedTopics,
            )
        }
    }
}

@Composable
fun TopicCard(
    topic: TopicListElement,
    homeScreenUiState: HomeScreenUiState,
    onCardButtonClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    trackedTopics: List<TrackedTopic>,
    navController: NavController
) {
    val expanded = homeScreenUiState.expandedTopicName == topic.name
    val enabled = !trackedTopics.any { it.name == topic.name }

    Card(
        shape = if (expanded) MaterialTheme.shapes.medium
        else RoundedCornerShape(bottomEnd = 16.dp, topStart = 16.dp),
        // animation when the  card is expanded or collapsed through modifier
        modifier = modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = topic.imageRes),
                contentDescription = null,
                modifier = Modifier.size(width = 68.dp, height = 68.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = stringResource(id = topic.name),
                style = MaterialTheme.typography.bodyMedium,
                color = if (expanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondaryContainer
            )
            TopicCardButton(
                topic = topic,
                expanded = expanded,
                onButtonClick = onCardButtonClick
            )
        }
        if (expanded) {
            Divider()
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(R.dimen.padding_medium)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        navController.navigate("${NavigationItem.TrackDetails.route}/${topic.name}") {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    enabled = enabled
                ) {
                    Icon(
                        tint = if (enabled) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onTertiary,
                        painter = painterResource(id = R.drawable.play_circle_24dp_fill0_wght400_grad0_opsz24),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = dimensionResource(R.dimen.padding_medium))
                    )
                }
                Text(
                    text =
                    if (enabled) {
                        buildAnnotatedString {
                            append("Start to Track your Progress about ")/*TODO change string*/
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(id = topic.name))
                            }
                        }
                    } else {
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(id = topic.name))
                            }
                            append(" is already being tracked!!!")/*TODO change string*/
                        }
                    },
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
                )
            }
        }
    }
}

@Composable
fun TopicCardButton(
    topic: TopicListElement,
    expanded: Boolean,
    onButtonClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { onButtonClick(topic.name) },
        modifier = modifier
    ) {
        Icon(
            imageVector = if(expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = stringResource(R.string.expand_button_content_description),
            tint = if(expanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondaryContainer)
    }
}
