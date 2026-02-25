package com.example.plantmood.presentation.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantmood.domain.model.DailyUsage
import com.example.plantmood.domain.model.PlantState
import com.example.plantmood.domain.utils.formatHoursToTime

@Composable
fun HomeScreen(
    state: PlantState,
    usage: DailyUsage,
    onRefresh: () -> Unit
) {

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))
    )

    val message = when (state) {
        PlantState.SEED ->
            "A fresh start 🌱 Keep building healthy habits."

        PlantState.GROWING ->
            "You're improving 🌿 Keep it up!"

        PlantState.BLOOMING ->
            "Amazing balance 🌸 Your habits are thriving!"

        PlantState.WITHERED ->
            "Your plant needs a break...\nmaybe you too 🧘"
    }

    val emoji = when (state) {
        PlantState.SEED -> "🌱"
        PlantState.GROWING -> "🌿"
        PlantState.BLOOMING -> "🌸"
        PlantState.WITHERED -> "🥀"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {

        Card(
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier.padding(24.dp),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {

            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Crossfade(targetState = emoji, label = "") { animatedEmoji ->
                    Text(
                        text = animatedEmoji,
                        fontSize = 110.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Current state: $state",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "⏱ Screen time: ${formatHoursToTime(usage.totalScreenTimeHours)}",
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "📱 Social media: ${formatHoursToTime(usage.socialMediaHours)}",
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "🌙 Used phone after midnight: ${
                        if (usage.usedAfterMidnight) "Yes" else "No"
                    }"
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = onRefresh,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7E57C2)
                    )
                ) {
                    Text(
                        text = "Refresh 🌿",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}