package com.example.trackthis.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.trackthis.R

data class Topic (
    @StringRes val name: Int,
    @DrawableRes val imageRes: Int
)
var listOfVisualizedTopics = mutableListOf(
    Topic(R.string.architecture, R.drawable.architecture),
    Topic(R.string.automotive, R.drawable.automotive),
    Topic(R.string.biology, R.drawable.biology),
    Topic(R.string.business, R.drawable.business),
    Topic(R.string.crafts, R.drawable.crafts,),
    Topic(R.string.culinary, R.drawable.culinary),
    Topic(R.string.design, R.drawable.design),
    Topic(R.string.drawing, R.drawable.drawing),
    Topic(R.string.ecology, R.drawable.ecology),
    Topic(R.string.engineering, R.drawable.engineering),
    Topic(R.string.fashion, R.drawable.fashion),
    Topic(R.string.film, R.drawable.film),
    Topic(R.string.finance, R.drawable.finance),
    Topic(R.string.gaming, R.drawable.gaming),
    Topic(R.string.geology, R.drawable.geology),
    Topic(R.string.history, R.drawable.history),
    Topic(R.string.journalism, R.drawable.journalism),
    Topic(R.string.law, R.drawable.law),
    Topic(R.string.lifestyle, R.drawable.lifestyle),
    Topic(R.string.music, R.drawable.music),
    Topic(R.string.painting, R.drawable.painting),
    Topic(R.string.photography, R.drawable.photography),
    Topic(R.string.physics, R.drawable.physics),
    Topic(R.string.tech, R.drawable.tech),
)

