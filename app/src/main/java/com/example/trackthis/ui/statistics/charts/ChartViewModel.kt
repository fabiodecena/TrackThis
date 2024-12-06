package com.example.trackthis.ui.statistics.charts


import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow



class ChartViewModel: ViewModel() {
    private val _chartUiState = MutableStateFlow(ChartUiState())
    val chartUiState: StateFlow<ChartUiState> = _chartUiState.asStateFlow()


    var dailyEffortInput by mutableStateOf("")

    init {
    _chartUiState.value = ChartUiState(defaultPointsData = pointsData, dailyEffort = dailyEffortList)
    }
}