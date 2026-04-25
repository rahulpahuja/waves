package com.rahulpahuja.waves.module.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun publishCourse(name: String, description: String, fee: Double, duration: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val course = Course(
                    name = name,
                    description = description,
                    fee = fee,
                    durationWeeks = duration
                )
                repository.saveCourse(course)
                onComplete()
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
}
