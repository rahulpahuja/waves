package com.rahulpahuja.waves.module.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminDashboardViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AdminDashboardUiState())
    val uiState: StateFlow<AdminDashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            // Simulate data loading
            _uiState.value = AdminDashboardUiState(
                studentCount = 142,
                studentGrowth = 5,
                revenue = 12.5,
                revenueGrowth = 12,
                sessions = listOf(
                    Session("18:00", "Intro to Mixing", "Studio A • Instructor: Alex"),
                    Session("19:30", "Scratch Techniques", "Studio B • Instructor: Sarah"),
                    Session("10:00", "Production Basics", "Studio C • Instructor: Mike")
                )
            )
        }
    }
}

data class AdminDashboardUiState(
    val studentCount: Int = 0,
    val studentGrowth: Int = 0,
    val revenue: Double = 0.0,
    val revenueGrowth: Int = 0,
    val sessions: List<Session> = emptyList()
)

data class Session(
    val time: String,
    val title: String,
    val details: String
)
