package com.example.trackthis.component.charts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.axis.AxisData
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.trackthis.R
import com.example.trackthis.data.pointsData
import com.example.trackthis.data.steps


@Composable
fun LineChartScreen(viewModel: ChartViewModel = viewModel()) {
    val yLabels = listOf(
        viewModel.getIndexForDay(viewModel._yLabels[0]),
        viewModel.getIndexForDay(viewModel._yLabels[1]),
        viewModel.getIndexForDay(viewModel._yLabels[2]),
        viewModel.getIndexForDay(viewModel._yLabels[3]),
        viewModel.getIndexForDay(viewModel._yLabels[4]),
        viewModel.getIndexForDay(viewModel._yLabels[5]),
        viewModel.getIndexForDay(viewModel._yLabels[6])
    )

    val xAxisData = AxisData.Builder()
        .axisStepSize(47.dp)
        .backgroundColor(Color.Transparent)
        .steps(pointsData.size - 1)
        .labelData { i -> yLabels[i].toString() }
        .labelAndAxisLinePadding(6.dp)
        .axisLineColor(MaterialTheme.colorScheme.onSecondaryContainer)
        .startDrawPadding(3.dp)
        .axisLabelAngle(20f)
        .axisLabelFontSize(10.sp)
        .axisLabelColor(MaterialTheme.colorScheme.onSecondaryContainer)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(steps)
        .backgroundColor(Color.Transparent)
        .labelAndAxisLinePadding(18.dp)
        .labelData { i ->
            val yScale = 24 / steps
            (i * yScale).toString()
        }
        .axisLineColor(MaterialTheme.colorScheme.onSecondaryContainer)
        .axisLabelColor(MaterialTheme.colorScheme.onSecondaryContainer)
        .build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(
                        color = MaterialTheme.colorScheme.primary,
                        lineType = LineType.SmoothCurve(isDotted = false)
                    ),
                    IntersectionPoint(
                        color = MaterialTheme.colorScheme.primary,
                    ),
                    SelectionHighlightPoint(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    ShadowUnderLine(
                        alpha = 0.5f,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.inversePrimary,
                                Color.Transparent
                            )
                        )

                    ),
                    SelectionHighlightPopUp()
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = null,
        backgroundColor = MaterialTheme.colorScheme.surface
    )
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(start = 3.dp),
        lineChartData = lineChartData
    )
}