package com.example.trackthis.ui.statistics


import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackthis.R
import com.example.trackthis.data.database.tracked_topic.TrackedTopic
import com.example.trackthis.ui.statistics.charts.ChartViewModel
import com.example.trackthis.ui.statistics.timer.TimerScreen
import com.example.trackthis.ui.statistics.timer.TimerUiState
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
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

/**
 * [StatisticsScreen] is a composable function that displays the statistics for a specific [TrackedTopic].
 *
 * It fetches data from [ChartViewModel] and [TimerViewModel] to display a line chart of progress,
 * daily effort indicators, and a timer interface. It also observing
 * a worker for resetting the tracked data on Mondays.
 *
 * @param modifier Modifier for styling the layout.
 * @param chartViewModel ViewModel for chart-related data.
 * @param timerViewModel ViewModel for timer-related data and actions.
 * @param topic The [TrackedTopic] for which to display statistics.
 * @param navController Navigation controller for navigating to other screens.
 */
@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    chartViewModel: ChartViewModel,
    timerViewModel: TimerViewModel,
    topic: TrackedTopic?,
    navController: NavController
) {
    val chartUiState by chartViewModel.chartUiState.collectAsState()
    val timerUiState by timerViewModel.timerUiState.collectAsState()
    val pointsData = chartUiState.defaultPointsData
    val dailyEffortList = chartUiState.dailyEffort
    val context = LocalContext.current


    LaunchedEffect(topic) {
        timerViewModel.updatePointsDataList(topic)
        if (topic != null) {
            timerViewModel.scheduleMondayResetWorker(context)
            timerViewModel.observeMondayResetWorker(context, navController, topic)
        }
        if (topic != null &&  timerUiState.timer == 0L) {
            timerViewModel.initializeTimer(topic)
        }
    }

    if (topic != null) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
        ) {
            StartedTopic(
                topicElement = topic,
                data = pointsData,
                dailyEffortList = dailyEffortList.map { topic.dailyEffort }
            )
            BuildTracking(
                timerViewModel = timerViewModel,
                topicElement = topic,
                timerUiState = timerUiState

            )
            TimerScreen(
                timerViewModel = timerViewModel,
                navController = navController,
                topicId = topic.name
            )
        }
    }
}

/**
 * [StartedTopic] displays a card containing a line chart of the weekly tracked topic's progress.
 *
 * @param topicElement The [TrackedTopic] for which to display the chart.
 * @param modifier Modifier for styling the layout.
 * @param data List of data points for the progress line chart.
 * @param dailyEffortList Set the minimum daily effort guessed by user. It remains the same each day of the week.
 */
@Composable
fun StartedTopic(
    topicElement: TrackedTopic,
    modifier: Modifier = Modifier,
    data: List<Double>,
    dailyEffortList: List<Double>
) {
    val dailyEffort = dailyEffortList.first()
    Card(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium)),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.weekly_effort),
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 22.dp),
                    data = remember {
                        listOf(
                            Line(
                                label = "Min Daily Effort",
                                values = dailyEffortList,
                                color = SolidColor(Color.Red),
                                drawStyle = DrawStyle.Stroke(
                                    strokeStyle = StrokeStyle.Dashed(
                                        intervals = floatArrayOf(10f, 10f)
                                    )
                                )
                            ),
                            Line(
                                label = "Progress",
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
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                        .horizontalScroll(rememberScrollState())
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
                    ) {
                        CircleWithLetter(
                            letter = stringResource(R.string.m_circle),
                            currentDay = DayOfWeek.MONDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                            topicElement = topicElement,
                            dailyEffort = dailyEffort
                        )
                        CircleWithLetter(
                            letter = stringResource(R.string.t_circle),
                            currentDay = DayOfWeek.TUESDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                            topicElement = topicElement,
                            dailyEffort = dailyEffort
                        )
                        CircleWithLetter(
                            letter = stringResource(R.string.w_circle),
                            currentDay = DayOfWeek.WEDNESDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                            topicElement = topicElement,
                            dailyEffort = dailyEffort
                        )
                        CircleWithLetter(
                            letter = stringResource(R.string.t_circle),
                            currentDay = DayOfWeek.THURSDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                            topicElement = topicElement,
                            dailyEffort = dailyEffort
                        )
                        CircleWithLetter(
                            stringResource(R.string.f_circle),
                            currentDay = DayOfWeek.FRIDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                            topicElement = topicElement,
                            dailyEffort = dailyEffort
                        )
                        CircleWithLetter(
                            stringResource(R.string.s_circle),
                            currentDay = DayOfWeek.SATURDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                            topicElement = topicElement,
                            dailyEffort = dailyEffort
                        )
                        CircleWithLetter(
                            stringResource(R.string.s_circle),
                            currentDay = DayOfWeek.SUNDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                            topicElement = topicElement,
                            dailyEffort = dailyEffort
                        )
                    }
                }
            }
        }
    }
}

/**
 * [CircleWithLetter] displays a circle with a letter inside it.
 *
 * The circle's background color is determined by the provided [topicElement]'s daily time spent
 * for the given [currentDay] in relation to the set [dailyEffort].
 *
 * @param letter The letter to display inside the circle.
 * @param currentDay The current day to check the time spent against.
 * @param topicElement The [TrackedTopic] containing the daily time spent data.
 * @param dailyEffort The daily effort target to compare against.
 */
@Composable
fun CircleWithLetter(
    letter: String, currentDay: String,
    topicElement: TrackedTopic,
    dailyEffort: Double
) {
    val isOnYAxis = topicElement.dailyTimeSpent[currentDay]?.let { it > 0 } ?: false
    val isLower = topicElement.dailyTimeSpent[currentDay]?.let { it < dailyEffort } ?: false
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(
                if (!isOnYAxis) MaterialTheme.colorScheme.onTertiary
                else if (isLower) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.tertiary
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

/**
 * [BuildTracking] displays a card with a play/pause button and the name of the tracked topic.
 *
 * This composable provides a visual representation of a tracked topic, including a button to start
 * tracking time and the topic's name. The button's enabled state depends on the [timerUiState].
 *
 * @param topicElement The [TrackedTopic] to display.
 * @param modifier Modifier for the layout.
 * @param timerViewModel The [TimerViewModel] to handle timer actions.
 * @param timerUiState The [TimerUiState] to determine the state of the timer.
 */
@Composable
fun BuildTracking(
    topicElement: TrackedTopic,
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel,
    timerUiState: TimerUiState
) {
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
                    enabled = !timerUiState.isTimerRunning || timerUiState.isPaused,
                    onClick = { timerViewModel.startTimer() },
                    modifier = Modifier
                        .padding(start = dimensionResource(R.dimen.padding_medium))
                ) {
                    Icon(
                        painterResource(R.drawable.play_circle_24dp_fill0_wght400_grad0_opsz24),
                        contentDescription = null,
                        tint = if (!timerUiState.isTimerRunning || timerUiState.isPaused) MaterialTheme.colorScheme.tertiary
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



