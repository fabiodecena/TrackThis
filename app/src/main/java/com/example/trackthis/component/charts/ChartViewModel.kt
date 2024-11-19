package com.example.trackthis.component.charts


import androidx.lifecycle.ViewModel
import com.example.trackthis.data.StartedTopicElement
import com.example.trackthis.data.listOfStartedTopic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow



class ChartViewModel: ViewModel() {
    private val _chartUiState = MutableStateFlow(ChartUiState())
    val chartUiState: StateFlow<ChartUiState> = _chartUiState.asStateFlow()

init {
    _chartUiState.value = ChartUiState(startedTopicList = listOfStartedTopic, pointsData = defaultPointsData)
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

}