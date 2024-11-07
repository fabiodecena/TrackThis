package com.example.trackthis.ui

import androidx.lifecycle.ViewModel
import com.example.trackthis.data.Topic
import com.example.trackthis.data.listOfVisualizedTopicListItem
import com.example.trackthis.data.listOfVisualizedTopics
import com.example.trackthis.visualizeTopics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel : ViewModel() {
    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState: StateFlow<AppUiState> = _appUiState.asStateFlow()

    private val visualizedTopics = visualizeTopics(
        listOfVisualizedTopic = listOfVisualizedTopics,
        listOfVisualizedTopicListItem = listOfVisualizedTopicListItem
    )
    fun updateTopicList(): List<Topic> {
        _appUiState.value = AppUiState(topicList = visualizedTopics)
        return _appUiState.value.topicList
    }
    fun toggleExpanded(topicName: Int) {
        _appUiState.update { currentUiState ->
            val newExpandedDog = currentUiState.expandedTopicName != topicName
            currentUiState.copy(
                expandedTopicName = if (newExpandedDog) topicName else 0,
            )
        }
    }
}