package com.example.trackthis.component.charts

import com.example.trackthis.data.StartedTopicElement


data class ChartUiState (
    val startedTopicList: List<StartedTopicElement> = emptyList(),
    val defaultPointsData: List<Double> = emptyList(),
    val yLabels: List<String> = listOf(
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday"
    )
)

val defaultPoints = mutableListOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0)
var pointsData = mutableListOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0)
