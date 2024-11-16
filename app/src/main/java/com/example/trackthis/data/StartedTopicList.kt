package com.example.trackthis.data

import androidx.annotation.StringRes

data class StartedTopicElement(
    @StringRes val name: Int,
)
var listOfStartedTopic = mutableListOf<StartedTopicElement>(

)




fun resetListOfStartedTopic() {// empty the whole list
    listOfStartedTopic.clear()
}