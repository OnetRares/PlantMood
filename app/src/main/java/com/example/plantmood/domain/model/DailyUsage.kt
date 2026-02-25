package com.example.plantmood.domain.model

import com.example.plantmood.data.local.UsageEntity
import java.util.Calendar

data class DailyUsage(
    val totalScreenTimeHours: Double,
    val socialMediaHours: Double,
    val usedAfterMidnight: Boolean
) {
    fun toEntity(): UsageEntity {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return UsageEntity(
            date = calendar.timeInMillis,
            totalScreenTimeHours = totalScreenTimeHours,
            socialMediaHours = socialMediaHours,
            usedAfterMidnight = usedAfterMidnight
        )
    }
}