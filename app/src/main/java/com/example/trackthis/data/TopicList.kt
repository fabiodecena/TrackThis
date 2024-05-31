package com.example.trackthis.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.trackthis.R

data class TopicListElement (
    @StringRes val name: Int,
    @DrawableRes val imageRes: Int,
    var selected: Boolean
)



var listOfVisualizedTopicListItem = mutableListOf(
    TopicListElement(R.string.architecture, R.drawable.architecture, true),
    TopicListElement(R.string.automotive, R.drawable.automotive, false),
    TopicListElement(R.string.law, R.drawable.law, true),
    TopicListElement(R.string.crafts, R.drawable.crafts, true),
    TopicListElement(R.string.business, R.drawable.business, true),
    TopicListElement(R.string.biology, R.drawable.biology, false),
    TopicListElement(R.string.ecology, R.drawable.ecology, true),
    TopicListElement(R.string.engineering, R.drawable.engineering, false),
    TopicListElement(R.string.finance, R.drawable.finance, false),
    TopicListElement(R.string.geology, R.drawable.geology, true),
    TopicListElement(R.string.culinary, R.drawable.culinary, true),
    TopicListElement(R.string.design, R.drawable.design, false),
    TopicListElement(R.string.fashion, R.drawable.fashion, false),
    TopicListElement(R.string.film, R.drawable.film, true),
    TopicListElement(R.string.gaming, R.drawable.gaming, true),
    TopicListElement(R.string.drawing,  R.drawable.drawing, false),
    TopicListElement(R.string.lifestyle,  R.drawable.lifestyle, true),
    TopicListElement(R.string.music, R.drawable.music, false),
    TopicListElement(R.string.painting, R.drawable.painting, true),
    TopicListElement(R.string.photography, R.drawable.photography, false),
    TopicListElement(R.string.tech, R.drawable.tech, false),
    TopicListElement(R.string.physics, R.drawable.physics, true),
    TopicListElement(R.string.history, R.drawable.history, false),
    TopicListElement(R.string.journalism, R.drawable.journalism, true)
).sortedBy { it.name }
