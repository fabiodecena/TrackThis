package com.example.trackthis.ui.statistics.charts


import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.trackthis.TrackApplication
import com.example.trackthis.ui.statistics.timer.TimerViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class ChartViewModel: ViewModel() {
    private val _chartUiState = MutableStateFlow(ChartUiState())
    val chartUiState: StateFlow<ChartUiState> = _chartUiState.asStateFlow()


    var dailyEffortInput by mutableStateOf("")

    init {
    _chartUiState.value = ChartUiState(defaultPointsData = pointsData, dailyEffort = dailyEffortList)
    }


    fun updateDailyEffort(dailyEffort: Double) {
        val updatedList: MutableList<Double> = dailyEffortList
        updatedList.indices.forEach { index ->
            updatedList[index] = dailyEffort
        }
        _chartUiState.update { currentState ->
            currentState.copy(dailyEffort = updatedList)
        }
    }

    fun clearList(){
        TimerViewModel(TrackApplication().database.trackedTopicDao()).resetData()
    }
}