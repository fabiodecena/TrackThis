package com.example.trackthis.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.trackthis.R

data class Topic (
    @StringRes val name: Int,
    val availableCourses: Int,
    @DrawableRes val imageRes: Int
)
var listOfVisualizedTopics = mutableListOf(
    Topic(R.string.architecture,58, R.drawable.architecture),
    Topic(R.string.automotive, 33, R.drawable.automotive),
    Topic(R.string.biology, 36, R.drawable.biology),
    Topic(R.string.business, 78, R.drawable.business),
    Topic(R.string.crafts, 121, R.drawable.crafts,),
    Topic(R.string.culinary, 118, R.drawable.culinary),
    Topic(R.string.design, 423, R.drawable.design),
    Topic(R.string.drawing, 326, R.drawable.drawing),
    Topic(R.string.ecology, 56, R.drawable.ecology),
    Topic(R.string.engineering, 112, R.drawable.engineering),
    Topic(R.string.fashion, 92, R.drawable.fashion),
    Topic(R.string.film, 165, R.drawable.film),
    Topic(R.string.finance, 22, R.drawable.finance),
    Topic(R.string.gaming, 164, R.drawable.gaming),
    Topic(R.string.geology, 78, R.drawable.geology),
    Topic(R.string.history, 225, R.drawable.history),
    Topic(R.string.journalism, 111, R.drawable.journalism),
    Topic(R.string.law, 99, R.drawable.law),
    Topic(R.string.lifestyle, 305, R.drawable.lifestyle),
    Topic(R.string.music, 212, R.drawable.music),
    Topic(R.string.painting, 172, R.drawable.painting),
    Topic(R.string.photography, 321, R.drawable.photography),
    Topic(R.string.physics, 233, R.drawable.physics),
    Topic(R.string.tech, 118, R.drawable.tech),
)

