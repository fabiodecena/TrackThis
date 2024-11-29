package com.example.trackthis.ui.home

import androidx.lifecycle.ViewModel
import com.example.trackthis.data.Topic
import com.example.trackthis.data.listOfVisualizedTopicListItem
import com.example.trackthis.data.listOfVisualizedTopics
import com.example.trackthis.visualizeTopics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeScreenViewModel : ViewModel() {
    private val _homeScreenUiState = MutableStateFlow(HomeScreenUiState())
    val homeScreenUiState: StateFlow<HomeScreenUiState> = _homeScreenUiState.asStateFlow()

    private val visualizedTopics = visualizeTopics(
        listOfVisualizedTopic = listOfVisualizedTopics,
        listOfVisualizedTopicListItem = listOfVisualizedTopicListItem
    )
    fun updateTopicList(): List<Topic> {
        _homeScreenUiState.value = HomeScreenUiState(topicList = visualizedTopics)
        return _homeScreenUiState.value.topicList
    }
    fun toggleExpanded(topicName: Int) {
        _homeScreenUiState.update { currentUiState ->
            val newExpandedDog = currentUiState.expandedTopicName != topicName
            currentUiState.copy(
                expandedTopicName = if (newExpandedDog) topicName else 0,
            )
        }
    }
}