package com.rahulpahuja.waves.module.admin.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahulpahuja.waves.data.remote.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val repository: FirestoreRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AttendanceUiState())
    val uiState: StateFlow<AttendanceUiState> = _uiState.asStateFlow()

    init {
        loadAttendanceData()
    }

    private fun loadAttendanceData() {
        viewModelScope.launch {
            repository.getUsers().collect { users ->
                val students = users.asSequence()
                    .filter { it.role == "student" }
                    .map {
                        AttendanceStudent(
                            id = it.uid,
                            name = it.displayName,
                            details = "Student • ${it.email}",
                            isOnline = false,
                            isPresent = false,
                        )
                    }
                    .toList()
                _uiState.update {
                    it.copy(
                        sessionTitle = "Today's Session",
                        sessionDate = "Oct 25, 2023",
                        sessionLocation = "Main Studio",
                        students = students,
                        filteredStudents = filterStudents(students, it.searchQuery)
                    )
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                filteredStudents = filterStudents(state.students, query)
            )
        }
    }

    fun toggleAttendance(studentId: String) {
        _uiState.update { state ->
            val updatedStudents = state.students.map {
                if (it.id == studentId) it.copy(isPresent = !it.isPresent) else it
            }
            state.copy(
                students = updatedStudents,
                filteredStudents = filterStudents(updatedStudents, state.searchQuery)
            )
        }
    }

    fun toggleSelectAll() {
        _uiState.update { state ->
            val allSelected = state.students.all { it.isPresent }
            val newValue = !allSelected
            val updatedStudents = state.students.map { it.copy(isPresent = newValue) }
            state.copy(
                students = updatedStudents,
                filteredStudents = filterStudents(updatedStudents, state.searchQuery)
            )
        }
    }

    private fun filterStudents(students: List<AttendanceStudent>, query: String): List<AttendanceStudent> {
        if (query.isBlank()) return students
        return students.filter {
            it.name.contains(query, ignoreCase = true) || it.details.contains(query, ignoreCase = true)
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
