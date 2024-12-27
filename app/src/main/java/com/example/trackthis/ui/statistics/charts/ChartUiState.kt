package com.example.trackthis.ui.statistics.charts

import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale


data class ChartUiState(
    val defaultPointsData: MutableList<Double> = mutableListOf(),
    val dailyEffort: List<Double> = emptyList(),
    val xLabels: List<String> = listOf(
        DayOfWeek.MONDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
        DayOfWeek.TUESDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
        DayOfWeek.WEDNESDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
        DayOfWeek.THURSDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
        DayOfWeek.FRIDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
        DayOfWeek.SATURDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
        DayOfWeek.SUNDAY.getDisplayName(TextStyle.FULL, Locale.getDefault())
    )
)

var pointsData = mutableListOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0)
var dailyEffortList = mutableListOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0)
