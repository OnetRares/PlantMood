package com.example.plantmood.data.local

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import com.example.plantmood.domain.model.DailyUsage
import java.util.Calendar
import java.util.concurrent.TimeUnit

class UsageStatsReader(private val context: Context) {

    fun getTodayUsage(): DailyUsage {

        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val startTime = calendar.timeInMillis

        val stats: List<UsageStats> =
            usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                startTime,
                endTime
            )

        var totalTimeMillis = 0L
        var socialMediaMillis = 0L

        val socialPackages = listOf(
            "com.instagram.android",
            "com.facebook.katana",
            "com.whatsapp",
            "com.twitter.android",
            "com.zhiliaoapp.musically",
            "com.snapchat.android"
        )

        for (usage in stats) {
            totalTimeMillis += usage.totalTimeInForeground

            if (socialPackages.contains(usage.packageName)) {
                socialMediaMillis += usage.totalTimeInForeground
            }
        }

        val totalHours =
            TimeUnit.MILLISECONDS.toMinutes(totalTimeMillis).toDouble() / 60.0

        val socialHours =
            TimeUnit.MILLISECONDS.toMinutes(socialMediaMillis).toDouble() / 60.0

        val usedAfterMidnight = totalHours > 0

        return DailyUsage(
            totalScreenTimeHours = totalHours,
            socialMediaHours = socialHours,
            usedAfterMidnight = usedAfterMidnight
        )
    }
    fun getLast7DaysAppUsage(): Map<String, Long> {

        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val startTime = calendar.timeInMillis

        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startTime,
            endTime
        )

        val appUsageMap = mutableMapOf<String, Long>()

        for (usage in stats) {
            val time = usage.totalTimeInForeground
            if (time > 0) {
                appUsageMap[usage.packageName] =
                    appUsageMap.getOrDefault(usage.packageName, 0L) + time
            }
        }

        return appUsageMap
    }
}