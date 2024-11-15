package com.example.trackthis.data

import co.yml.charts.common.model.Point

data class LineChartDataClass(
    val pointsData: List<Point>,
    val steps: Int,
)
const val steps = 6
var pointsData = listOf(
    Point(0f, 0f),
    Point(1f, 0f),
    Point(2f, 0f),
    Point(3f, 0f),
    Point(4f, 0f),
    Point(5f, 0f),
    Point(6f, 0f),
)