package com.example.trackthis.ui.statistics.charts


data class ChartUiState(
    val defaultPointsData: MutableList<Double> = mutableListOf(),
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
