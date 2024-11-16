package com.example.trackthis.component.charts


import androidx.lifecycle.ViewModel
import co.yml.charts.common.model.Point
import com.example.trackthis.data.StartedTopicElement
import com.example.trackthis.data.listOfStartedTopic

import com.example.trackthis.data.pointsData

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class ChartViewModel: ViewModel() {
    private val _chartUiState = MutableStateFlow(ChartUiState())
    val chartUiState: StateFlow<ChartUiState> = _chartUiState.asStateFlow()

    fun updateStartedTopicList(): List<StartedTopicElement> {
        _chartUiState.value = ChartUiState(startedTopicList = listOfStartedTopic)
        return _chartUiState.value.startedTopicList
    }
    fun addStartedTopicElementToList(topicName: Int) {
        if (StartedTopicElement(topicName) !in listOfStartedTopic)// avoid to add the same Topic more than once
            listOfStartedTopic.add(StartedTopicElement(topicName))
    }
    fun removeStartedTopicElementFromList(topicName: Int): MutableList<StartedTopicElement> {// remove one element from the list
        listOfStartedTopic.remove(StartedTopicElement(topicName))
        return listOfStartedTopic
    }

    fun updatePointsDataList(): List<Point> {
        _chartUiState.value = ChartUiState(pointsData = pointsData)
        return _chartUiState.value.pointsData
    }


    val _yLabels = listOf(
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday"
    )

    fun getIndexForDay(day: String): Int {
        return _yLabels.indexOf(day)
    }
}