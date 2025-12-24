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
class CreateSessionViewModel @Inject constructor() : ViewModel() {

    private val _sessionName = MutableStateFlow("")
    val sessionName: StateFlow<String> = _sessionName.asStateFlow()

    private val _selectedCategory = MutableStateFlow("DJing")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _startTime = MutableStateFlow("06:00 PM")
    val startTime: StateFlow<String> = _startTime.asStateFlow()

    private val _endTime = MutableStateFlow("08:00 PM")
    val endTime: StateFlow<String> = _endTime.asStateFlow()

    private val _capacity = MutableStateFlow(12)
    val capacity: StateFlow<Int> = _capacity.asStateFlow()

    private val _selectedStudio = MutableStateFlow("Main Room")
    val selectedStudio: StateFlow<String> = _selectedStudio.asStateFlow()

    private val _uiState = MutableStateFlow<CreateSessionUiState>(CreateSessionUiState.Idle)
    val uiState: StateFlow<CreateSessionUiState> = _uiState.asStateFlow()

    fun onSessionNameChanged(name: String) {
        _sessionName.value = name
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun onStartTimeChanged(time: String) {
        _startTime.value = time
    }

    fun onEndTimeChanged(time: String) {
        _endTime.value = time
    }

    fun onCapacityChanged(capacity: Int) {
        _capacity.value = capacity
    }

    fun onStudioSelected(studio: String) {
        _selectedStudio.value = studio
    }

    fun publishSession() {
        viewModelScope.launch {
            _uiState.value = CreateSessionUiState.Loading
            // Simulate API call
            try {
                // api.createSession(...)
                _uiState.value = CreateSessionUiState.Success
            } catch (e: Exception) {
                _uiState.value = CreateSessionUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class CreateSessionUiState {
    object Idle : CreateSessionUiState()
    object Loading : CreateSessionUiState()
    object Success : CreateSessionUiState()
    data class Error(val message: String) : CreateSessionUiState()
}
