package com.example.trackthis.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.trackthis.R
import com.example.trackthis.ui.home.HomeScreen

/**
 * Represents a topic with a name, an associated image, and a selection state.
 *
 * @property name The string resource ID for the topic's name.
 * @property imageRes The drawable resource ID for the topic's image.
 * @property selected Indicates whether the topic is currently selected. Defaults to `false`.
 */
data class TopicListElement(
    @StringRes val name: Int,
    @DrawableRes val imageRes: Int,
    var selected: Boolean = true
)

/**
 * A mutable list of [TopicListElement] instances, representing all available topics.
 *
 * This list is initialized with a set of predefined topics, each with a name
 * (referenced by a string resource), an image (referenced by a drawable resource),
 * and an initial selection state (defaulting to `true`).
 * The selection state indicates whether a topic is currently active or inactive and displayed within the [HomeScreen]
 *
 * The list is sorted alphabetically by the topic's name.
 */
var listOfVisualizedTopicListItem = mutableListOf(
    TopicListElement(R.string.architecture, R.drawable.architecture),
    TopicListElement(R.string.automotive, R.drawable.automotive),
    TopicListElement(R.string.law, R.drawable.law),
    TopicListElement(R.string.crafts, R.drawable.crafts),
    TopicListElement(R.string.business, R.drawable.business),
    TopicListElement(R.string.biology, R.drawable.biology),
    TopicListElement(R.string.ecology, R.drawable.ecology),
    TopicListElement(R.string.engineering, R.drawable.engineering),
    TopicListElement(R.string.finance, R.drawable.finance),
    TopicListElement(R.string.geology, R.drawable.geology),
    TopicListElement(R.string.culinary, R.drawable.culinary),
    TopicListElement(R.string.design, R.drawable.design),
    TopicListElement(R.string.fashion, R.drawable.fashion),
    TopicListElement(R.string.film, R.drawable.film),
    TopicListElement(R.string.gaming, R.drawable.gaming),
    TopicListElement(R.string.drawing,  R.drawable.drawing),
    TopicListElement(R.string.lifestyle,  R.drawable.lifestyle),
    TopicListElement(R.string.music, R.drawable.music),
    TopicListElement(R.string.painting, R.drawable.painting),
    TopicListElement(R.string.photography, R.drawable.photography),
    TopicListElement(R.string.tech, R.drawable.tech),
    TopicListElement(R.string.physics, R.drawable.physics),
    TopicListElement(R.string.history, R.drawable.history),
    TopicListElement(R.string.journalism, R.drawable.journalism)
).sortedBy { it.name }

