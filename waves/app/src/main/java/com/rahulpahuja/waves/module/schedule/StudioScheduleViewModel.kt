package com.rahulpahuja.waves.module.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudioScheduleViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(StudioScheduleUiState())
    val uiState: StateFlow<StudioScheduleUiState> = _uiState.asStateFlow()

    init {
        loadSchedule()
    }

    private fun loadSchedule() {
        viewModelScope.launch {
            _uiState.value = StudioScheduleUiState(
                selectedDate = 5,
                availableSlots = listOf(
                    Slot("1", "Studio A - Mixing", "Available • 1h Session", "10:00", "AM", true),
                    Slot("2", "DJ Booth - Pioneer", "Occupied", "11:00", "AM", false),
                    Slot("3", "Production Suite", "Available • 2h Session", "02:00", "PM", true)
                ),
                myBookings = listOf(
                    Booking("1", "Intro to Mixing", "Studio A", "Today, 4:00 PM", BookingStatus.CONFIRMED),
                    Booking("2", "Open Deck Session", "DJ Booth", "Sep 7, 6:00 PM", BookingStatus.PENDING),
                    Booking("3", "Music Theory 101", "Classroom B", "Aug 28, 10:00 AM", BookingStatus.MISSED)
                )
            )
        }
    }

    fun onDateSelected(date: Int) {
        // Logic to update schedule based on date
    }
}

data class StudioScheduleUiState(
    val selectedDate: Int = 1,
    val availableSlots: List<Slot> = emptyList(),
    val myBookings: List<Booking> = emptyList()
)

data class Slot(
    val id: String,
    val title: String,
    val subtitle: String,
    val time: String,
    val period: String,
    val isAvailable: Boolean
)

data class Booking(
    val id: String,
    val title: String,
    val location: String,
    val dateTime: String,
    val status: BookingStatus
)

enum class BookingStatus { CONFIRMED, PENDING, MISSED }
