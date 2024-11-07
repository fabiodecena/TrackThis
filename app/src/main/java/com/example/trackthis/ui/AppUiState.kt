package com.example.trackthis.ui

import com.example.trackthis.data.Topic

data class AppUiState(
    val topicList: List<Topic> = emptyList(),
    val expandedTopicName: Int = 0
)