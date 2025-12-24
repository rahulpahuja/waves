package com.rahulpahuja.waves.module.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentDashboardViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(StudentDashboardUiState())
    val uiState: StateFlow<StudentDashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            // Simulate data loading
            _uiState.value = StudentDashboardUiState(
                userName = "DJ Kicks",
                currentCourse = CourseProgress(
                    title = "Music Production 101",
                    progressPercentage = 0.75f,
                    completedClasses = 12,
                    totalClasses = 16
                ),
                nextSession = Session(
                    day = "24",
                    month = "OCT",
                    title = "Advanced Serato Tech",
                    time = "18:00 - 20:00",
                    location = "Studio B"
                ),
                notification = "Masterclass with DJ Snake confirmed for Nov 15th"
            )
        }
    }
}

data class StudentDashboardUiState(
    val userName: String = "",
    val currentCourse: CourseProgress? = null,
    val nextSession: Session? = null,
    val notification: String? = null
)

data class CourseProgress(
    val title: String,
    val progressPercentage: Float,
    val completedClasses: Int,
    val totalClasses: Int
)

data class Session(
    val day: String,
    val month: String,
    val title: String,
    val time: String,
    val location: String
)
