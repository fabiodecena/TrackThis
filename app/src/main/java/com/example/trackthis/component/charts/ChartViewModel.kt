package com.example.trackthis.component.charts



import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.trackthis.data.StartedTopicElement
import com.example.trackthis.data.listOfStartedTopic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class ChartViewModel: ViewModel() {
    private val _chartUiState = MutableStateFlow(ChartUiState())
    val chartUiState: StateFlow<ChartUiState> = _chartUiState.asStateFlow()

init {
    _chartUiState.value = ChartUiState(startedTopicList = listOfStartedTopic, defaultPointsData = defaultPointsData)
}

    fun updateStartedTopicList(): List<StartedTopicElement> {
        _chartUiState.value = ChartUiState(startedTopicList = listOfStartedTopic)
        return _chartUiState.value.startedTopicList
    }
    fun addStartedTopicElementToList(topicName: Int) {
        if (StartedTopicElement(topicName) !in listOfStartedTopic)// avoid to add the same Topic more than once
            listOfStartedTopic.add(StartedTopicElement(topicName))
    }
    fun removeStartedTopicElementFromList(topicName: Int){// remove one element from the list
        listOfStartedTopic.remove(StartedTopicElement(topicName))
        updateStartedTopicList()
    }

    fun updatePointsDataList(index: Int, value: Long) {
        Log.d("ChartViewModel", "afbefore change: ${_chartUiState.value.defaultPointsData.joinToString()}")
        val updatedList = _chartUiState.value.defaultPointsData.toMutableList()
        updatedList[index] = value.toDouble()
        _chartUiState.update { currentState ->
            currentState.copy(defaultPointsData = updatedList)
        }
        Log.d("ChartViewModel", "after change: ${_chartUiState.value.defaultPointsData.joinToString()}")
    }

    fun getIndexForDay(day: String): Int {
        return chartUiState.value.yLabels.indexOf(day)
    }
}