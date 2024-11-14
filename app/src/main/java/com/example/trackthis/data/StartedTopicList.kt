package com.example.trackthis.data

import androidx.annotation.StringRes

data class StartedTopicElement(
    @StringRes val name: Int,
)
var listOfStartedTopic = mutableListOf<StartedTopicElement>(

)

fun addStartedTopicElementToList(topicName: Int) {
    if (StartedTopicElement(topicName) !in listOfStartedTopic)// avoid to add the same Topic more than once
        listOfStartedTopic.add(StartedTopicElement(topicName))
}

fun removeStartedTopicElementFromList(topicName: Int): MutableList<StartedTopicElement> {// remove one element from the list
    listOfStartedTopic.remove(StartedTopicElement(topicName))
    return listOfStartedTopic
}

fun resetListOfStartedTopic() {// empty the whole list
    listOfStartedTopic.clear()
}