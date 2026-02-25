package com.example.plantmood.domain.usecase

import com.example.plantmood.data.local.UsageEntity
import com.example.plantmood.domain.model.DailyUsage
import com.example.plantmood.domain.model.PlantState

class CalculatePlantStateUseCase {

    fun execute(
        today: DailyUsage,
        last7Days: List<UsageEntity>
    ): PlantState {

        if (last7Days.isEmpty()) return PlantState.SEED

        val average =
            last7Days.map { it.totalScreenTimeHours }
                .average()

        return when {
            today.totalScreenTimeHours < average * 0.7 ->
                PlantState.BLOOMING

            today.totalScreenTimeHours < average ->
                PlantState.GROWING

            today.totalScreenTimeHours < average * 1.3 ->
                PlantState.SEED

            else ->
                PlantState.WITHERED
        }
    }
}