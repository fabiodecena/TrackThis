package com.example.trackthis.ui.home

import com.example.trackthis.data.TopicListElement

data class HomeScreenUiState(
    val topicList: List<TopicListElement> = emptyList(),
    val expandedTopicName: Int = 0
)