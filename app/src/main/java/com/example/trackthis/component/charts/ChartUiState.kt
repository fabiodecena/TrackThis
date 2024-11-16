package com.example.trackthis.component.charts

import co.yml.charts.common.model.Point
import com.example.trackthis.data.StartedTopicElement

data class ChartUiState (
    val startedTopicList: List<StartedTopicElement> = emptyList(),
    val pointsData: List<Point> = emptyList(),
    val steps: Int = 0,

)