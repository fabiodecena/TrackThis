package com.example.trackthis.ui.home

import com.example.trackthis.data.TopicListElement

/**
 * [HomeScreenUiState] is a data class that represents the UI state of the [HomeScreen].
 *
 * @param topicList The list of [TopicListElement]s to be displayed on the home screen.
 *   Defaults to an empty list.
 * @param expandedTopicName The ID of the topic that is currently expanded, or 0 if no topic is expanded.
 */
data class HomeScreenUiState(
    val topicList: List<TopicListElement> = emptyList(),
    val expandedTopicName: Int = 0
)