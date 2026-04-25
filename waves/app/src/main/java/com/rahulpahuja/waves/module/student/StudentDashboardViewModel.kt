package com.rahulpahuja.waves.module.student

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.rahulpahuja.waves.data.remote.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentDashboardViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repository: FirestoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudentDashboardUiState())
    val uiState: StateFlow<StudentDashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            val userName = try {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    repository.getUser(currentUser.uid)?.displayName
                        ?: currentUser.displayName
                        ?: ""
                } else ""
            } catch (e: Exception) {
                Log.e("StudentDashboardVM", "Error loading user", e)
                auth.currentUser?.displayName ?: ""
            }

            _uiState.value = StudentDashboardUiState(
                userName = userName,
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
