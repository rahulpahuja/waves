package com.rahulpahuja.waves.module.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahulpahuja.waves.data.remote.Announcement
import com.rahulpahuja.waves.data.remote.Course
import com.rahulpahuja.waves.data.remote.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateSessionViewModel @Inject constructor(
    private val repository: FirestoreRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun publishCourse(
        name: String,
        description: String,
        fee: Double,
        duration: Int,
        topics: List<String>,
        category: String,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val course = Course(
                    name = name,
                    description = description,
                    fee = fee,
                    durationWeeks = duration,
                    topics = topics,
                    category = category
                )
                repository.saveCourse(course)
                repository.addAnnouncement(
                    Announcement(
                        title = "New Course: $name",
                        body = "$category · ₹${fee.toLong()} · ${topics.size} topics",
                        courseId = course.id
                    )
                )
                onComplete()
            } catch (e: Exception) {
                // error handled via isLoading reset
            } finally {
                _isLoading.value = false
            }
        }
    }
}
