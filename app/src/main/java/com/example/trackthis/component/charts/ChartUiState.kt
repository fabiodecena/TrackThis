package com.example.trackthis.component.charts


import com.example.trackthis.data.StartedTopicElement

data class ChartUiState (
        val startedTopicList: List<StartedTopicElement> = emptyList(),
        val steps: Int = 0
)