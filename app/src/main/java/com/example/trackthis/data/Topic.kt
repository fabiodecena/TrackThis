package com.example.trackthis.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.trackthis.R

data class Topic (
    @StringRes val name: Int,
    val availableCourses: Int,
    @DrawableRes val imageRes: Int,
    var expanded: Boolean
)
var listOfVisualizedTopics = mutableListOf(
    Topic(R.string.architecture,58, R.drawable.architecture, false),
    Topic(R.string.automotive, 33, R.drawable.automotive, false),
    Topic(R.string.biology, 36, R.drawable.biology, false),
    Topic(R.string.business, 78, R.drawable.business, false),
    Topic(R.string.crafts, 121, R.drawable.crafts, false),
    Topic(R.string.culinary, 118, R.drawable.culinary, false),
    Topic(R.string.design, 423, R.drawable.design, false),
    Topic(R.string.drawing, 326, R.drawable.drawing, false),
    Topic(R.string.ecology, 56, R.drawable.ecology, false),
    Topic(R.string.engineering, 112, R.drawable.engineering, false),
    Topic(R.string.fashion, 92, R.drawable.fashion, false),
    Topic(R.string.film, 165, R.drawable.film, false),
    Topic(R.string.finance, 22, R.drawable.finance, false),
    Topic(R.string.gaming, 164, R.drawable.gaming, false),
    Topic(R.string.geology, 78, R.drawable.geology, false),
    Topic(R.string.history, 225, R.drawable.history, false),
    Topic(R.string.journalism, 111, R.drawable.journalism, false),
    Topic(R.string.law, 99, R.drawable.law, false),
    Topic(R.string.lifestyle, 305, R.drawable.lifestyle, false),
    Topic(R.string.music, 212, R.drawable.music, false),
    Topic(R.string.painting, 172, R.drawable.painting, false),
    Topic(R.string.photography, 321, R.drawable.photography, false),
    Topic(R.string.physics, 233, R.drawable.physics, false),
    Topic(R.string.tech, 118, R.drawable.tech, false),
)

