package com.example.trackthis.ui.home

import com.example.trackthis.data.Topic

data class HomeScreenUiState(
    val topicList: List<Topic> = emptyList(),
    val expandedTopicName: Int = 0
)