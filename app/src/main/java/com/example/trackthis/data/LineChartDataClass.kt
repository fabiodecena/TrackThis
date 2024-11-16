package com.example.trackthis.data

import androidx.compose.runtime.mutableStateListOf
import co.yml.charts.common.model.Point

data class LineChartDataClass(
    val pointsData: List<Point>,
    val steps: Int,
)
const val steps = 6
var pointsData = mutableStateListOf(
    Point(0f, 0f),
    Point(1f, 10f),
    Point(2f, 50f),
    Point(3f, 30f),
    Point(4f, 0f),
    Point(5f, 0f),
    Point(6f, 60f),
)