package com.example.plantmood.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.plantmood.domain.utils.formatHoursToTime
import com.example.plantmood.presentation.home.components.WeeklyBarChart

@Composable
fun StatsScreen(viewModel: HomeViewModel) {

    val weeklyData by viewModel.weeklyUsage.collectAsStateWithLifecycle()
    val topApps by viewModel.topApps.collectAsStateWithLifecycle()
    val weeklySocial by viewModel.weeklySocialUsage.collectAsStateWithLifecycle()

    val average =
        if (weeklyData.isNotEmpty()) weeklyData.average()
        else 0.0

    val best = weeklyData.minOrNull() ?: 0f

    val totalScreenTime = weeklyData.sum()
    val totalSocialTime = weeklySocial.sum()

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF3E5F5),
            Color(0xFFEDE7F6)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(20.dp)
    ) {

        Text(
            text = "📊 Last 7 Days",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {

                WeeklyBarChart(weeklyData)

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Average per day: ${formatHoursToTime(average.toDouble())}",
                    fontSize = 16.sp
                )

                Text(
                    text = "Best day: ${formatHoursToTime(best.toDouble())}",
                    fontSize = 16.sp,
                    color = Color(0xFF6A1B9A)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Divider()

                Spacer(modifier = Modifier.height(20.dp))


                Text(
                    text = "Total screen time (7 days)",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = formatHoursToTime(totalScreenTime.toDouble()),
                    color = getDynamicColor(totalScreenTime),
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))


                Text(
                    text = "Total social media (7 days)",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = formatHoursToTime(totalSocialTime.toDouble()),
                    color = getDynamicColor(totalSocialTime),
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Divider()

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Most used apps",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                topApps.forEach { (appName, hours) ->
                    Text(
                        text = "$appName: ${formatHoursToTime(hours)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

private fun getDynamicColor(totalHours: Float): Color {
    return when {
        totalHours > 35f -> Color(0xFFD32F2F)
        totalHours > 20f -> Color(0xFFF57C00)
        else -> Color(0xFF388E3C)
    }
}