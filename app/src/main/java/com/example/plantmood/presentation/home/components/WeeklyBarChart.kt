package com.example.plantmood.presentation.home.components

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs

@Composable
fun WeeklyBarChart(
    screenTimes: List<Float>
) {

    val days = remember {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("dd.MM", Locale.getDefault())

        (6 downTo 0).map {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -it)
            format.format(calendar.time)
        }
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp),
        factory = { context ->

            val chart = BarChart(context)

            val entries = screenTimes.mapIndexed { index, value ->
                BarEntry(index.toFloat(), value)
            }

            val dataSet = BarDataSet(entries, "")

            val average =
                if (screenTimes.isNotEmpty()) screenTimes.average().toFloat()
                else 0f


            val colors = screenTimes.map { hours ->

                when {
                    hours > average + 1.5f ->
                        Color.parseColor("#D32F2F")

                    hours > average + 0.5f ->
                        Color.parseColor("#F57C00")

                    hours < average - 1.5f ->
                        Color.parseColor("#388E3C")

                    else ->
                        Color.parseColor("#9575CD")
                }
            }

            dataSet.colors = colors
            dataSet.valueTextSize = 13f
            dataSet.valueTextColor = Color.BLACK
            dataSet.setDrawValues(true)

            val barData = BarData(dataSet)
            barData.barWidth = 0.65f

            chart.data = barData

            // Stilizare
            chart.description.isEnabled = false
            chart.legend.isEnabled = false
            chart.axisRight.isEnabled = false

            chart.axisLeft.setDrawGridLines(false)
            chart.axisLeft.axisMinimum = 0f

            chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(days)
            chart.xAxis.granularity = 1f
            chart.xAxis.labelRotationAngle = -45f
            chart.xAxis.setDrawGridLines(false)

            chart.setFitBars(true)
            chart.animateY(800)

            chart.invalidate()
            chart
        }
    )
}