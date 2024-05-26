package com.example.trackthis.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.trackthis.R

data class TopicListItem (
    @StringRes val name: Int,
    @DrawableRes val imageRes: Int,
    var selected: Boolean
)


var listOfVisualizedTopicListItem = mutableListOf(
    TopicListItem(R.string.architecture, R.drawable.architecture, true),
    TopicListItem(R.string.automotive, R.drawable.automotive, false),
    TopicListItem(R.string.law, R.drawable.law, true),
    TopicListItem(R.string.crafts, R.drawable.crafts, true),
    TopicListItem(R.string.business, R.drawable.business, true),
    TopicListItem(R.string.biology, R.drawable.biology, false),
    TopicListItem(R.string.ecology, R.drawable.ecology, true),
    TopicListItem(R.string.engineering, R.drawable.engineering, false),
    TopicListItem(R.string.finance, R.drawable.finance, false),
    TopicListItem(R.string.geology, R.drawable.geology, true),
    TopicListItem(R.string.culinary, R.drawable.culinary, true),
    TopicListItem(R.string.design, R.drawable.design, false),
    TopicListItem(R.string.fashion, R.drawable.fashion, false),
    TopicListItem(R.string.film, R.drawable.film, true),
    TopicListItem(R.string.gaming, R.drawable.gaming, true),
    TopicListItem(R.string.drawing,  R.drawable.drawing, false),
    TopicListItem(R.string.lifestyle,  R.drawable.lifestyle, true),
    TopicListItem(R.string.music, R.drawable.music, false),
    TopicListItem(R.string.painting, R.drawable.painting, true),
    TopicListItem(R.string.photography, R.drawable.photography, false),
    TopicListItem(R.string.tech, R.drawable.tech, false),
    TopicListItem(R.string.physics, R.drawable.physics, true),
    TopicListItem(R.string.history, R.drawable.history, false),
    TopicListItem(R.string.journalism, R.drawable.journalism, true)
)
