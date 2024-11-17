package com.example.trackthis.component.charts

import androidx.compose.runtime.mutableStateListOf
import co.yml.charts.common.model.Point
import com.example.trackthis.data.StartedTopicElement
import kotlin.div
import kotlin.times

data class ChartUiState (
    val startedTopicList: List<StartedTopicElement> = emptyList(),
    val pointsData: MutableList<Point> = mutableStateListOf(
        Point(0f, 0f),
        Point(1f, 0f),
        Point(2f, 0f),
        Point(3f, 0f),
        Point(4f, 0f),
        Point(5f, 0f),
        Point(6f, 0f)
    ),

    val steps: Int = 0,

)