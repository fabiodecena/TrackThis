package com.example.trackthis.data

import androidx.annotation.StringRes
import com.example.trackthis.R

data class StartedTopicElement(
    @StringRes val name: Int,
)
var listOfStartedTopic = mutableListOf<StartedTopicElement>(

)

fun addStartedTopicElementToList(topicName: Int) {
    listOfStartedTopic.add(StartedTopicElement(topicName))
}
fun resetListOfStartedTopic() {
    listOfStartedTopic.clear()
}