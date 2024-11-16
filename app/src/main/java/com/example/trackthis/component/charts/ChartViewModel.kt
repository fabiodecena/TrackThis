package com.example.trackthis.component.charts

import androidx.lifecycle.ViewModel
import com.example.trackthis.R

class ChartViewModel: ViewModel() {
    val _yLabels = listOf(
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday"
    )

    fun getIndexForDay(day: String): Int {
        return _yLabels.indexOf(day)
    }
}