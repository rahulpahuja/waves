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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
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
        val uid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            val userName = try {
                repository.getUser(uid)?.displayName ?: auth.currentUser?.displayName ?: ""
            } catch (e: Exception) {
                Log.e("StudentDashboardVM", "Error loading user", e)
                auth.currentUser?.displayName ?: ""
            }
            _uiState.value = _uiState.value.copy(userName = userName)
        }

        viewModelScope.launch {
            combine(
                repository.getUserEnrollments(uid),
                repository.getCourses(),
                repository.getAnnouncements()
            ) { enrollments, courses, announcements ->
                Triple(enrollments, courses, announcements)
            }.catch { e ->
                Log.e("StudentDashboardVM", "Error collecting dashboard data: ${e.message}")
            }.collect { (enrollments, courses, announcements) ->
                val latestAnnouncement = announcements.firstOrNull()?.let {
                    "${it.title} — ${it.body}"
                }

                val courseProgress = enrollments.firstOrNull()?.let { enrollment ->
                    val course = courses.find { it.id == enrollment.courseId }
                    if (course != null) {
                        val total = course.topics.size.coerceAtLeast(1)
                        val done = enrollment.completedTopics.size
                        CourseProgress(
                            title = course.name,
                            progressPercentage = done.toFloat() / total,
                            completedClasses = done,
                            totalClasses = total
                        )
                    } else null
                }

                _uiState.value = _uiState.value.copy(
                    currentCourse = courseProgress,
                    notification = latestAnnouncement
                )
            }
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
