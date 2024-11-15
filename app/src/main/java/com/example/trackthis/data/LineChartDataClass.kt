package com.example.trackthis.data

import co.yml.charts.common.model.Point

data class LineChartDataClass(
    val pointsData: List<Point>,
    val steps: Int,
)
const val steps = 6
var pointsData = listOf(
    Point(0f, 40f),
    Point(1f, 90f),
    Point(2f, 0f),
    Point(3f, 60f),
    Point(4f, 10f),
    Point(5f, 50f),
    Point(6f, 80f),
)