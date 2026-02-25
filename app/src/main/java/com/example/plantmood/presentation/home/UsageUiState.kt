package com.example.plantmood.presentation.home

data class UsageUiState(
    val todayHours: Float = 0f,
    val weeklyHours: List<Float> = emptyList()
)