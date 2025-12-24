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
class StudentsViewModel @Inject constructor() : ViewModel() {

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    init {
        loadStudents()
    }

    private fun loadStudents() {
        // Simulate data loading
        viewModelScope.launch {
            _students.value = listOf(
                Student("Alex \"DJ Orbit\" Rivera", "Advanced Mixing & Mastering", true),
                Student("Sarah Jenkins", "DJ 101 - Intro to Vinyl", false),
                Student("Marcus Chen", "Production Level II", true),
                Student("Jessica Davis", "Logic Pro Essentials", false),
                Student("David Miller", "Sound Design", false),
                Student("Elena Rodriguez", "Ableton Live Masterclass", false)
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterSelected(filter: String) {
        _selectedFilter.value = filter
    }
}
