package com.rahulpahuja.waves.module.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AttendanceUiState())
    val uiState: StateFlow<AttendanceUiState> = _uiState.asStateFlow()

    init {
        loadAttendanceData()
    }

    private fun loadAttendanceData() {
        viewModelScope.launch {
            // Simulate data loading
            val students = listOf(
                AttendanceStudent("1", "Alex 'Reverb' Smith", "ID: DJ-102 • Level 1", true, true),
                AttendanceStudent("2", "Sarah 'Spin' Jones", "ID: DJ-105 • Level 2", false, true),
                AttendanceStudent("3", "Mike 'Drop' Davis", "ID: DJ-108 • Level 1", false, false),
                AttendanceStudent("4", "Jessica 'Beat' Lee", "ID: DJ-112 • Level 3", true, true),
                AttendanceStudent("5", "David 'Fader' Wilson", "ID: DJ-115 • Level 2", false, false),
                AttendanceStudent("6", "Emily 'Mix' Clark", "ID: DJ-120 • Level 1", true, true)
            )
            _uiState.update { 
                it.copy(
                    sessionTitle = "Advanced Scratching Techniques",
                    sessionDate = "Oct 24, 18:00 - 20:00",
                    sessionLocation = "Studio B • Instr. Grandmaster Flash",
                    students = students,
                    filteredStudents = students
                ) 
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { state ->
            val filtered = state.students.filter { 
                it.name.contains(query, ignoreCase = true) || it.details.contains(query, ignoreCase = true)
            }
            state.copy(searchQuery = query, filteredStudents = filtered)
        }
    }

    fun toggleAttendance(studentId: String) {
        _uiState.update { state ->
            val updatedStudents = state.students.map { 
                if (it.id == studentId) it.copy(isPresent = !it.isPresent) else it 
            }
            // Re-apply filter if needed, or just update the source list and re-filter
            val filtered = updatedStudents.filter { 
                it.name.contains(state.searchQuery, ignoreCase = true) 
            }
            state.copy(students = updatedStudents, filteredStudents = filtered)
        }
    }

    fun toggleSelectAll() {
        _uiState.update { state ->
            val allSelected = state.students.all { it.isPresent }
            val newValue = !allSelected
            val updatedStudents = state.students.map { it.copy(isPresent = newValue) }
             val filtered = updatedStudents.filter { 
                it.name.contains(state.searchQuery, ignoreCase = true) 
            }
            state.copy(students = updatedStudents, filteredStudents = filtered)
        }
    }

    fun submitAttendance() {
        viewModelScope.launch {
            // Simulate API call
        }
    }
}

data class AttendanceUiState(
    val sessionTitle: String = "",
    val sessionDate: String = "",
    val sessionLocation: String = "",
    val students: List<AttendanceStudent> = emptyList(),
    val filteredStudents: List<AttendanceStudent> = emptyList(),
    val searchQuery: String = ""
) {
    val presentCount: Int
        get() = students.count { it.isPresent }
    
    val totalCount: Int
        get() = students.size
}

data class AttendanceStudent(
    val id: String,
    val name: String,
    val details: String,
    val isOnline: Boolean,
    val isPresent: Boolean
)
