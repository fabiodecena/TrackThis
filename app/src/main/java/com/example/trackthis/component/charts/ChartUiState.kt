package com.example.trackthis.component.charts

import com.example.trackthis.data.StartedTopicElement


data class ChartUiState (
    val startedTopicList: List<StartedTopicElement> = emptyList(),
    val defaultPointsData: List<Double> = emptyList(),
    val dailyEffort: List<Double> = emptyList(),
    val xLabels: List<String> = listOf(
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday"
    )
)

var pointsData = mutableListOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0)
var dailyEffortList = mutableListOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0)
