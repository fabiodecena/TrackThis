package com.example.trackthis.ui.statistics.charts

import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale
/**
 * [ChartUiState] represents the UI state for a chart.
 *
 * @property defaultPointsData A mutable list of Double values representing the default data points for the chart.
 *   Defaults to an empty mutable list.
 * @property dailyEffort A list of Double values representing the daily effort data for the chart.
 *   Defaults to an empty list.
 * @property xLabels A list of Strings representing the labels for the x-axis of the chart.
 *   Defaults to a list of the full names of the days of the week, starting with Monday,
 *   based on the default locale.
 */
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
/**
 *  A mutable list of Double values representing the points data for the chart.
 *  Initialized with 7 values of 0.0.
 */
var pointsData = mutableListOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0)
/**
 * A mutable list of Double values representing the daily effort data for the chart.
 * Initialized with 7 values of 0.0.
 */
var dailyEffortList = mutableListOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0)
