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
class ManageBookingsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ManageBookingsUiState())
    val uiState: StateFlow<ManageBookingsUiState> = _uiState.asStateFlow()

    init {
        loadBookings()
    }

    private fun loadBookings() {
        viewModelScope.launch {
            _uiState.value = ManageBookingsUiState(
                date = "Thursday, Oct 5",
                stats = "3 Slots Full • 2 Pending",
                pendingRequests = listOf(
                    BookingRequest("1", "Alex D.", "Intermediate • Mixing", "2:00 PM - 4:00 PM", "Studio B"),
                    BookingRequest("2", "Marcus T.", "Advanced • Mastering", "10:00 AM @ Production Lab", "")
                ),
                confirmedSessions = listOf(
                    ConfirmedSession("1", "Sarah J.", "DJ Booth 1", "5:00 PM", "2h duration"),
                    ConfirmedSession("2", "Jen K.", "Studio A", "7:30 PM", "1h duration")
                )
            )
        }
    }

    fun approveRequest(id: String) {
        // Logic to approve
    }

    fun rejectRequest(id: String) {
        // Logic to reject
    }
}

data class ManageBookingsUiState(
    val date: String = "",
    val stats: String = "",
    val pendingRequests: List<BookingRequest> = emptyList(),
    val confirmedSessions: List<ConfirmedSession> = emptyList()
)

data class BookingRequest(
    val id: String,
    val name: String,
    val details: String,
    val time: String,
    val location: String
)

data class ConfirmedSession(
    val id: String,
    val name: String,
    val location: String,
    val time: String,
    val duration: String
)
