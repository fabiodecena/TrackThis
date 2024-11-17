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
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
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
import com.example.trackthis.data.pointsData
import com.example.trackthis.data.steps



@Composable
fun LineChartScreen() {
    val yLabels = listOf(
        "Mon",
        "Tue",
        "Wed",
        "Thu",
        "Fri",
        "Sat",
        "Sun"
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
            val maxValue = pointsData.maxOf { it.y } // Get the maximum data value
            val yScale = maxValue / steps
            (i * yScale).toInt().toString()
        }
        .axisLineColor(MaterialTheme.colorScheme.onSecondaryContainer)
        .axisLabelColor(MaterialTheme.colorScheme.onSecondaryContainer)
        .build()

    val dailyEffort = listOf(
        Point(0f,4f),
        Point(1f,4f),
        Point(2f,4f),
        Point(3f,4f),
        Point(4f,4f),
        Point(5f,4f),
        Point(6f,4f)
    )
    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = dailyEffort,
                    LineStyle(
                        color = Color.Red,
                        lineType = LineType.Straight(isDotted = true)
                    )
                ),
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