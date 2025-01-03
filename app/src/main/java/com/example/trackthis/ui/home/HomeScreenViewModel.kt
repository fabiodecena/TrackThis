package com.example.trackthis.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.trackthis.TrackApplication
import com.example.trackthis.data.TopicListElement
import com.example.trackthis.data.TopicListRepository
import com.example.trackthis.data.listOfVisualizedTopicListItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [HomeScreenViewModel] is a ViewModel class that manages the state of the [HomeScreen].
 *
 * It is responsible for:
 * - Holding and updating the UI state ([HomeScreenUiState]).
 * - Managing the list of topics ([TopicListElement]) and their selection status.
 * - Communicating with the [TopicListRepository] to persist selected topics.
 * - Handling user interactions such as topic selection and expansion.
 */
class HomeScreenViewModel(private val topicListRepository: TopicListRepository) : ViewModel() {
    // Backing property to avoid state updates from other classes
    private val _homeScreenUiState = MutableStateFlow(HomeScreenUiState())
    /**
     *  Exposes the [HomeScreenUiState] to the UI.
     */
    val homeScreenUiState: StateFlow<HomeScreenUiState> = _homeScreenUiState.asStateFlow()
    // Backing property to avoid state updates from other classes
    private val _topics = MutableStateFlow(HomeScreenUiState().topicList.toMutableList())
    /**
     * Exposes the list of topics to the UI.
     * The list is updated when the user selects or deselects a topic.
     */
    val topics: StateFlow<List<TopicListElement>> = _topics.asStateFlow()

    init {
        // Observe selected topics from repository
        viewModelScope.launch {
            topicListRepository.selectedTopics.collect { selectedTopicNames ->
                val updatedList = listOfVisualizedTopicListItem.map { topic ->
                    topic.copy(selected = selectedTopicNames.contains(topic.name.toString()))
                }
                _topics.value = updatedList.toMutableList()
            }
        }
    }
    /**
     * Handles the selection of a [TopicListElement].
     *
     * @param topic The topic that was selected.
     *
     * This function updates the topic's selection status in the list and persists the changes
     * using the [TopicListRepository].
     */
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
    /**
     * Returns a list of the currently selected topics.
     *
     * @return A list of [TopicListElement] that are currently selected.
     */
    fun updateTopicList(): List<TopicListElement> {
        // Ensure active topics are reflected dynamically
        return _topics.value.filter { it.selected }
    }
    /**
     * Toggles the expansion state of a topic.
     *
     * @param topicName The name of the topic to toggle.
     *
     * If the topic is currently expanded, it will be collapsed.
     * If the topic is currently collapsed, it will be expanded.
     */
    fun toggleExpanded(topicName: Int) {
        _homeScreenUiState.update { currentUiState ->
            val newExpandedDog = currentUiState.expandedTopicName != topicName
            currentUiState.copy(
                expandedTopicName = if (newExpandedDog) topicName else 0,
            )
        }
    }

    companion object {
        /**
         * Factory for creating [HomeScreenViewModel] instances.
         */
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as TrackApplication
                HomeScreenViewModel( application.topicListRepository)
            }
        }
    }
}