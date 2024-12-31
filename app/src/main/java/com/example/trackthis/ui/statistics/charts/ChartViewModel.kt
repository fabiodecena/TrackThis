package com.example.trackthis.ui.statistics.charts


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class ChartViewModel: ViewModel() {
    private val _chartUiState = MutableStateFlow(ChartUiState())
    val chartUiState: StateFlow<ChartUiState> = _chartUiState.asStateFlow()

    init {
    _chartUiState.value = ChartUiState(defaultPointsData = pointsData, dailyEffort = dailyEffortList)
    }
}