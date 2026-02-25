package com.example.plantmood.domain.utils

fun formatHoursToTime(hoursDecimal: Double): String {
    val totalMinutes = (hoursDecimal * 60).toInt()
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    return if (minutes == 0) {
        "${hours}h"
    } else {
        "${hours}h ${minutes}min"
    }
}