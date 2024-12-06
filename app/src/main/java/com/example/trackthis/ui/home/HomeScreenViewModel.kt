package com.example.trackthis.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.trackthis.TrackApplication
import com.example.trackthis.data.Topic
import com.example.trackthis.data.TopicListElement
import com.example.trackthis.data.TopicListRepository
import com.example.trackthis.data.listOfVisualizedTopicListItem
import com.example.trackthis.data.listOfVisualizedTopics
import com.example.trackthis.visualizeTopics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val topicListRepository: TopicListRepository) : ViewModel() {
    private val _homeScreenUiState = MutableStateFlow(HomeScreenUiState())
    val homeScreenUiState: StateFlow<HomeScreenUiState> = _homeScreenUiState.asStateFlow()
    private val visualizedTopics = visualizeTopics(
        listOfVisualizedTopic = listOfVisualizedTopics,
        listOfVisualizedTopicListItem = listOfVisualizedTopicListItem
    )
    private val _topics = MutableStateFlow<List<TopicListElement>>(emptyList())
    val topics: StateFlow<List<TopicListElement>> = _topics

    init {
        // Observe saved topics and update the list
        viewModelScope.launch {
            topicListRepository.selectedTopics.collect { savedSelectedTopics ->
                val updatedList = listOfVisualizedTopicListItem.map { topic ->
                    topic.copy(selected = savedSelectedTopics.contains(topic.name.toString()))
                }
                _topics.value = updatedList
            }
        }
    }

    fun onTopicSelected(topic: TopicListElement) {
        viewModelScope.launch {
            val updatedTopics = _topics.value.toMutableList()
            val updatedTopic = topic.copy(selected = !topic.selected)
            val index = updatedTopics.indexOfFirst { it.name == topic.name }
            if (index != -1) {// Check if the index is valid
                updatedTopics[index] = updatedTopic
                _topics.value = updatedTopics

                // Persist selected topics
                val selectedTopicNames = updatedTopics.filter { it.selected }.map { it.name.toString() }.toSet()
                topicListRepository.saveSelectedTopics(selectedTopicNames)
            }
        }
    }

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

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as TrackApplication
                HomeScreenViewModel( application.topicListRepository)
            }
        }
    }
}