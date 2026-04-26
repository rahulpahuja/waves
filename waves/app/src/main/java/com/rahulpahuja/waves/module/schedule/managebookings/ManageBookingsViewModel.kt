package com.rahulpahuja.waves.module.schedule.managebookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahulpahuja.waves.data.remote.FirestoreRepository
import com.rahulpahuja.waves.data.remote.BookingRequest as FirestoreBookingRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ManageBookingsViewModel @Inject constructor(
    private val repository: FirestoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageBookingsUiState())
    val uiState: StateFlow<ManageBookingsUiState> = _uiState.asStateFlow()

    init {
        observeRequests()
    }

    private fun observeRequests() {
        viewModelScope.launch {
            repository.getBookingRequests().collectLatest { requests ->
                _uiState.value = _uiState.value.copy(
                    pendingRequests = requests.map { it.toUiModel() }
                )
            }
        }
    }

    fun approveRequest(id: String) {
        viewModelScope.launch {
            repository.updateBookingStatus(id, "APPROVED")
        }
    }

    fun rejectRequest(id: String, reason: String) {
        viewModelScope.launch {
            repository.updateBookingStatus(id, "REJECTED", rejectionReason = reason)
        }
    }

    private fun FirestoreBookingRequest.toUiModel(): BookingRequestUI {
        val timeStr = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(this.startTime))
        return BookingRequestUI(
            id = this.id,
            name = this.userName,
            details = if (this.type == "CHECKIN") "Live Check-in" else "Scheduled Booking",
            time = timeStr,
            location = "Studio" // Default
        )
    }
}

data class ManageBookingsUiState(
    val date: String = SimpleDateFormat("EEEE, MMM dd", Locale.getDefault()).format(Date()),
    val stats: String = "",
    val pendingRequests: List<BookingRequestUI> = emptyList(),
    val confirmedSessions: List<ConfirmedSession> = emptyList()
)

data class BookingRequestUI(
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
