package com.example.plantmood.presentation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MainScreen(viewModel: HomeViewModel) {

    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.BarChart, contentDescription = "Stats") },
                    label = { Text("Stats") }
                )
            }
        }
    ) { _ ->

        when (selectedTab) {

            0 -> {
                val state by viewModel.plantState.collectAsStateWithLifecycle()
                val usage by viewModel.usage.collectAsStateWithLifecycle()

                HomeScreen(
                    state = state,
                    usage = usage,
                    onRefresh = { viewModel.loadPlantState() }
                )
            }

            1 -> {
                StatsScreen(viewModel)
            }
        }
    }
}