package com.example.trackthis.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.navigation.NavController
import com.example.trackthis.R
import com.example.trackthis.data.Topic
import com.example.trackthis.data.trackDetails
import com.example.trackthis.ui.AppUiState
import com.example.trackthis.ui.AppViewModel

@Composable
fun TopicCard(
    topic: Topic,
    viewModel: AppViewModel,
    appUiState: AppUiState,
    onCardButtonClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val expanded = appUiState.expandedTopicName == topic.name

    Card(
        shape = if (expanded) MaterialTheme.shapes.medium
            else RoundedCornerShape(bottomEnd = 16.dp, topStart = 16.dp),
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
                onButtonClick = onCardButtonClick)
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
                Icon(
                    tint = MaterialTheme.colorScheme.tertiary,
                    painter = painterResource(id = R.drawable.play_circle_24dp_fill0_wght400_grad0_opsz24),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = dimensionResource(R.dimen.padding_medium))
                        .clickable {
                            navController.navigate("${trackDetails[0].route}/${topic.name}") {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                )
                Text(
                    text = buildAnnotatedString {
                        append("Start to Track your Progress about ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(id = topic.name))
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
    topic: Topic,
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
