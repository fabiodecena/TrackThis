package com.example.trackthis.ui.statistics.charts


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * [ChartViewModel] is a ViewModel class responsible for managing the UI state for a chart.
 *
 * This ViewModel holds and manages the [ChartUiState], which represents the data
 * required to render a chart in the UI. It initializes the state with default data
 * and exposes it as a [StateFlow] for observation by the UI.
 *
 * The ViewModel is designed to survive configuration changes and provide a stable
 * data source for the chart.
 */
class ChartViewModel: ViewModel() {
    private val _chartUiState = MutableStateFlow(ChartUiState())
    val chartUiState: StateFlow<ChartUiState> = _chartUiState.asStateFlow()

    init {
    _chartUiState.value = ChartUiState(defaultPointsData = pointsData, dailyEffort = dailyEffortList)
    }
}