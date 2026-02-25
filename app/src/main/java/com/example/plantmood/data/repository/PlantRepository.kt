package com.example.plantmood.data.repository

import android.content.Context
import com.example.plantmood.data.local.AppDatabase
import com.example.plantmood.data.local.UsageStatsReader
import com.example.plantmood.domain.model.DailyUsage
import com.example.plantmood.domain.model.PlantState
import com.example.plantmood.domain.usecase.CalculatePlantStateUseCase

class PlantRepository(
    private val context: Context,
    private val calculateUseCase: CalculatePlantStateUseCase
) {

    private val database = AppDatabase.getDatabase(context)
    private val dao = database.usageDao()

    suspend fun getTodayData(): Pair<DailyUsage, PlantState> {

        val usageReader = UsageStatsReader(context)
        val todayUsage = usageReader.getTodayUsage()

        // 🔥 salvăm azi (cu dată unică)
        dao.insertUsage(todayUsage.toEntity())

        val last7Entities = dao.getLast7Days()

        val state = calculateUseCase.execute(
            today = todayUsage,
            last7Days = last7Entities
        )

        return Pair(todayUsage, state)
    }

    suspend fun getLast7DaysUsage(): List<Double> {
        return dao.getLast7Days()
            .sortedBy { it.date }
            .map { it.totalScreenTimeHours }
    }
    suspend fun getTopAppsLast7Days(): Map<String, Double> {

        val reader = UsageStatsReader(context)
        val rawData = reader.getLast7DaysAppUsage()

        return rawData
            .filter { it.value > 60_000 }
            .mapValues { it.value / 1000.0 / 60.0 / 60.0 }
            .toList()
            .sortedByDescending { it.second }
            .take(5)
            .toMap()
    }

    suspend fun getLast7DaysSocialUsage(): List<Double> {

        val data = dao.getLast7Days()
            .sortedBy { it.date }
            .map { it.socialMediaHours }

        return if (data.size >= 7) {
            data.takeLast(7)
        } else {
            val padded = MutableList(7 - data.size) { 0.0 }
            padded.addAll(data)
            padded
        }
    }
}