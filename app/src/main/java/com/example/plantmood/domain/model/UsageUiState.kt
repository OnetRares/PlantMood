package com.example.plantmood.domain.model

data class UsageUiState(
    val todayHours: Float = 0f,
    val weeklyHours: List<Float> = emptyList()
)