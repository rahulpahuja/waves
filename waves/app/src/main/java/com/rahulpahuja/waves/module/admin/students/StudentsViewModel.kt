package com.rahulpahuja.waves.module.admin.students

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
class StudentsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(StudentsUiState())
    val uiState: StateFlow<StudentsUiState> = _uiState.asStateFlow()

    init {
        loadStudents()
    }

    private fun loadStudents() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            // Simulate data loading
            kotlinx.coroutines.delay(300)
            val students = listOf(
                Student("Alex \"DJ Orbit\" Rivera", "Advanced Mixing & Mastering", true),
                Student("Sarah Jenkins", "DJ 101 - Intro to Vinyl", false),
                Student("Marcus Chen", "Production Level II", true),
                Student("Jessica Davis", "Logic Pro Essentials", false),
                Student("David Miller", "Sound Design", false),
                Student("Elena Rodriguez", "Ableton Live Masterclass", false)
            )
            _uiState.update { 
                it.copy(
                    students = students,
                    filteredStudents = students,
                    isLoading = false
                ) 
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { state ->
            val filtered = state.students.filter { 
                it.name.contains(query, ignoreCase = true) || it.course.contains(query, ignoreCase = true)
            }
            state.copy(searchQuery = query, filteredStudents = filtered)
        }
    }

    fun onFilterSelected(filter: String) {
        _uiState.update { state ->
            val filtered = if (filter == "All") state.students else state.students.filter { it.course.contains(filter, ignoreCase = true) }
            state.copy(selectedFilter = filter, filteredStudents = filtered)
        }
    }
}

data class StudentsUiState(
    val isLoading: Boolean = false,
    val students: List<Student> = emptyList(),
    val filteredStudents: List<Student> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: String = "All"
)
