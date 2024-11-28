package com.example.trackthis.screen


import android.util.Log
import androidx.compose.animation.core.EaseInOutCubic
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackthis.R
import com.example.trackthis.ui.charts.pointsData
import com.example.trackthis.data.NavigationItem
import com.example.trackthis.data.StartedTopicElement
import com.example.trackthis.ui.timer.TimerViewModel
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
fun StartedTopic(
    topicElement: StartedTopicElement,
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
                    CircleWithLetter("M", index = 0)
                    CircleWithLetter("T", index = 1)
                    CircleWithLetter("W", index = 2)
                    CircleWithLetter("T", index = 3)
                    CircleWithLetter("F", index = 4)
                    CircleWithLetter("S", index = 5)
                    CircleWithLetter("S", index = 6)
                    }
                }
            }
        }
    }
}


@Composable
fun CircleWithLetter(letter: String, index: Int) {
    val isOnYAxis = pointsData.getOrNull(index)?.let { it > 0.0 } == false
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(
                if (isOnYAxis) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.tertiary
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
