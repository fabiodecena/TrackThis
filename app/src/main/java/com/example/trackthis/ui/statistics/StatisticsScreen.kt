package com.example.trackthis.ui.statistics

import android.util.Log
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.copy
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackthis.R
import com.example.trackthis.data.NavigationItem
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.ui.insert_track.TrackEntryViewModel
import com.example.trackthis.ui.statistics.charts.ChartViewModel
import com.example.trackthis.ui.statistics.charts.pointsData
import com.example.trackthis.ui.statistics.timer.TimerScreen
import com.example.trackthis.ui.statistics.timer.TimerViewModel
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.StrokeStyle
import ir.ehsannarmani.compose_charts.models.ZeroLineProperties


@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    chartViewModel: ChartViewModel,
    timerViewModel: TimerViewModel,
    trackEntryViewModel: TrackEntryViewModel,
    navController: NavController
) {
    val chartUiState by chartViewModel.chartUiState.collectAsState()
    val pointsData = chartUiState.defaultPointsData
    val dailyEffort = chartUiState.dailyEffort

    val trackedTopics by trackEntryViewModel.retrieveAllItems().collectAsState(emptyList())

    val firstTopic = trackedTopics.firstOrNull() // Get the first topic

    val updatedPointsData = pointsData.toMutableList().also { list ->
        val index = firstTopic?.index
        pointsData.forEachIndexed { it, _ ->
            if (it == index) {
                list[index] = firstTopic.timeSpent.toDouble()
            }
        }
    }

    if (firstTopic != null) { // Conditionally render UI for the first topic
        Column(modifier = modifier) {
            StartedTopic(
                topicElement = firstTopic,
                onDelete = { chartViewModel.clearList() },
                data = updatedPointsData,
                dailyEffort = dailyEffort.map { firstTopic.dailyEffort }, // Use firstTopic's dailyEffort
                navController = navController,
                timerViewModel = timerViewModel
            )
            BuildTracking(
                timerViewModel = timerViewModel,
                topicElement = firstTopic
            )
            TimerScreen(
                timerViewModel = timerViewModel,
                navController = navController,
                topicId = firstTopic.name
            )
        }
    }
}

@Composable
fun StartedTopic(
    topicElement: TrackedTopic,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    data: List<Double>,
    dailyEffort: List<Double>,
    timerViewModel: TimerViewModel,
    navController: NavController
) {
    Card(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium)),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                    text = stringResource(topicElement.name),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.weekly_effort),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
            FloatingActionButton(
                onClick = {
                    onDelete()
                    timerViewModel.resetTimer()
                    navController.navigate(NavigationItem.Statistics.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route)
                        }
                    }
                },
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_medium2))
                    .padding(top = dimensionResource(R.dimen.padding_medium), start = dimensionResource(R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete Element",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                    .height(200.dp)
                    .width(350.dp),
                shape = MaterialTheme.shapes.large
            ){
                LineChart(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 22.dp),
                    data = remember {
                        Log.d("pointsData", "StartedTopic: $data")
                        listOf(
                            Line(
                                label = "Min Daily Effort",
                                values = dailyEffort,
                                color = SolidColor(Color.Red),
                                drawStyle = DrawStyle.Stroke(
                                    strokeStyle = StrokeStyle.Dashed(
                                        intervals = floatArrayOf(10f, 10f)
                                    )
                                )
                            ),
                            Line(
                                label = "User Name2",
                                values = data,
                                color = SolidColor(Color(0xFF23af92)),
                                dotProperties = DotProperties(
                                    enabled = true,
                                    color = SolidColor(Color.White),
                                    strokeWidth = 2.dp,
                                    radius = 2.dp,
                                    strokeColor = SolidColor(Color.Magenta),
                                ),
                                firstGradientFillColor = Color(0xFF2BC0A1).copy(alpha = .5f),
                                secondGradientFillColor = Color.Transparent,
                                strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                                gradientAnimationDelay = 1000,
                                drawStyle = DrawStyle.Stroke(width = 2.dp),
                            )
                        )
                    },
                    labelProperties = LabelProperties(
                        enabled = true,
                        labels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                    ),
                    gridProperties = GridProperties(
                        enabled = false
                    ),
                    zeroLineProperties = ZeroLineProperties(
                        enabled = true,
                        color = SolidColor(MaterialTheme.colorScheme.error),
                    ),
                    maxValue = 24.0,
                    animationMode = AnimationMode.Together(delayBuilder = {
                        it * 500L
                    }),
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_medium))
                    .height(70.dp)
                    .width(350.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
                    ) {
                        CircleWithLetter(
                            "M", index = 0,
                            topicElement = topicElement
                        )
                        CircleWithLetter(
                            "T", index = 1,
                            topicElement = topicElement
                        )
                        CircleWithLetter(
                            "W", index = 2,
                            topicElement = topicElement
                        )
                        CircleWithLetter(
                            "T", index = 3,
                            topicElement = topicElement
                        )
                        CircleWithLetter(
                            "F", index = 4,
                            topicElement = topicElement
                        )
                        CircleWithLetter(
                            "S", index = 5,
                            topicElement = topicElement
                        )
                        CircleWithLetter(
                            "S", index = 6,
                            topicElement = topicElement
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CircleWithLetter(letter: String, index: Int, topicElement: TrackedTopic) {
    val isOnYAxis = topicElement.index == index && topicElement.index > 0
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(
                if (!isOnYAxis) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.tertiary
            )
            .size(dimensionResource(R.dimen.padding_medium2))
    ) {
        Text(
            text = letter,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun BuildTracking(
    topicElement: TrackedTopic,
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel
) {
    val timerValue = timerViewModel.timer.collectAsState()
    val isPaused = timerViewModel.isPaused.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = modifier
                .padding(dimensionResource(R.dimen.padding_medium))
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Row(
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.padding_medium))
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    enabled = timerValue.value == 0L || isPaused.value,
                    onClick = { timerViewModel.startTimer() },
                    modifier = Modifier
                        .padding(start = dimensionResource(R.dimen.padding_medium))
                ) {
                    Icon(
                        painterResource(R.drawable.play_circle_24dp_fill0_wght400_grad0_opsz24),
                        contentDescription = null,
                        tint = if (timerValue.value == 0L || isPaused.value) MaterialTheme.colorScheme.tertiary
                            else MaterialTheme.colorScheme.onTertiary
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(end = dimensionResource(R.dimen.padding_medium2)),
                    text = stringResource(topicElement.name),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}
