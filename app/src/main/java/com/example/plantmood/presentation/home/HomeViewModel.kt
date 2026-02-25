package com.example.plantmood.presentation.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantmood.data.repository.PlantRepository
import com.example.plantmood.domain.model.DailyUsage
import com.example.plantmood.domain.model.PlantState
import com.example.plantmood.domain.usecase.CalculatePlantStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = PlantRepository(
        application,
        CalculatePlantStateUseCase()
    )

    // 🌿 Plant state
    private val _plantState = MutableStateFlow(PlantState.SEED)
    val plantState: StateFlow<PlantState> = _plantState

    // 📱 Today usage
    private val _usage = MutableStateFlow(
        DailyUsage(0.0, 0.0, false)
    )
    val usage: StateFlow<DailyUsage> = _usage

    // 📊 Weekly usage (last 7 days)
    private val _weeklyUsage = MutableStateFlow<List<Float>>(emptyList())
    val weeklyUsage: StateFlow<List<Float>> = _weeklyUsage

    private val _weeklySocialUsage = MutableStateFlow<List<Float>>(emptyList())
    val weeklySocialUsage: StateFlow<List<Float>> = _weeklySocialUsage

    private val _topApps = MutableStateFlow<Map<String, Double>>(emptyMap())
    val topApps: StateFlow<Map<String, Double>> = _topApps
    init {
        loadPlantState()
    }

    fun loadPlantState() {
        viewModelScope.launch {

            val (todayUsage, plantState) = repository.getTodayData()

            _usage.value = todayUsage
            _plantState.value = plantState
            _weeklySocialUsage.value = repository.getLast7DaysSocialUsage().map { it.toFloat() }
            _topApps.value = repository.getTopAppsLast7Days()

            val last7Days = repository.getLast7DaysUsage()

            _weeklyUsage.value =
                if (last7Days.size >= 7) {
                    last7Days.takeLast(7).map { it.toFloat() }
                } else {

                    val padded = MutableList(7 - last7Days.size) { 0.0 }
                    padded.addAll(last7Days)
                    padded.map { it.toFloat() }
                }
        }
    }
}