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
class CreateStudentViewModel @Inject constructor() : ViewModel() {

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _selectedProgram = MutableStateFlow<String?>(null)
    val selectedProgram: StateFlow<String?> = _selectedProgram.asStateFlow()

    private val _startDate = MutableStateFlow("")
    val startDate: StateFlow<String> = _startDate.asStateFlow()

    private val _uiState = MutableStateFlow<CreateStudentUiState>(CreateStudentUiState.Idle)
    val uiState: StateFlow<CreateStudentUiState> = _uiState.asStateFlow()

    fun onFullNameChanged(name: String) {
        _fullName.value = name
    }

    fun onPhoneNumberChanged(number: String) {
        _phoneNumber.value = number
    }

    fun onEmailChanged(email: String) {
        _email.value = email
    }

    fun onProgramSelected(program: String) {
        _selectedProgram.value = program
    }

    fun onStartDateChanged(date: String) {
        _startDate.value = date
    }

    fun completeEnrollment() {
        viewModelScope.launch {
            _uiState.value = CreateStudentUiState.Loading
            // Simulate API call
            try {
                // api.createStudent(...)
                _uiState.value = CreateStudentUiState.Success
            } catch (e: Exception) {
                _uiState.value = CreateStudentUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class CreateStudentUiState {
    object Idle : CreateStudentUiState()
    object Loading : CreateStudentUiState()
    object Success : CreateStudentUiState()
    data class Error(val message: String) : CreateStudentUiState()
}
