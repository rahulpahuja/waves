package com.rahulpahuja.waves.module.schedule.studioschedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.rahulpahuja.waves.data.remote.BookingRequest
import com.rahulpahuja.waves.data.remote.FirestoreRepository
import com.rahulpahuja.waves.data.remote.StudioAvailability
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StudioScheduleViewModel @Inject constructor(
    private val repository: FirestoreRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudioScheduleUiState())
    val uiState: StateFlow<StudioScheduleUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            // Observe availability
            repository.getStudioAvailability().collectLatest { availability ->
                _uiState.value = _uiState.value.copy(studioAvailability = availability ?: StudioAvailability())
                generateSlots()
            }
        }
        viewModelScope.launch {
            // Observe user bookings
            repository.getUserBookings(uid).collectLatest { bookings ->
                _uiState.value = _uiState.value.copy(myBookings = bookings)
            }
        }
    }

    private fun generateSlots() {
        val availability = _uiState.value.studioAvailability
        val slots = mutableListOf<Slot>()
        
        // Simplified slot generation for 9 AM to 5 PM
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val startCalendar = Calendar.getInstance().apply {
            time = sdf.parse(availability.startTime) ?: Date()
        }
        val endCalendar = Calendar.getInstance().apply {
            time = sdf.parse(availability.endTime) ?: Date()
        }

        while (startCalendar.before(endCalendar)) {
            val timeStr = SimpleDateFormat("hh:mm", Locale.getDefault()).format(startCalendar.time)
            val period = SimpleDateFormat("a", Locale.getDefault()).format(startCalendar.time)
            
            slots.add(Slot(
                id = startCalendar.timeInMillis.toString(),
                title = "Studio Session",
                subtitle = "Available • 1h",
                time = timeStr,
                period = period,
                isAvailable = true,
                timestamp = startCalendar.timeInMillis
            ))
            startCalendar.add(Calendar.HOUR_OF_DAY, 1)
        }
        
        _uiState.value = _uiState.value.copy(availableSlots = slots)
    }

    fun requestBooking(slot: Slot) {
        val user = auth.currentUser ?: return
        val request = BookingRequest(
            userId = user.uid,
            userName = user.displayName ?: "User",
            startTime = slot.timestamp,
            durationMinutes = 60,
            type = "BOOKING",
            status = "PENDING"
        )
        viewModelScope.launch {
            try {
                repository.createBookingRequest(request)
            } catch (e: Exception) {
                android.util.Log.e("StudioScheduleVM", "Failed to request booking: ${e.message}", e)
            }
        }
    }

    fun onDateSelected(date: Int) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
    }
}

data class StudioScheduleUiState(
    val selectedDate: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
    val studioAvailability: StudioAvailability = StudioAvailability(),
    val availableSlots: List<Slot> = emptyList(),
    val myBookings: List<BookingRequest> = emptyList()
)

data class Slot(
    val id: String,
    val title: String,
    val subtitle: String,
    val time: String,
    val period: String,
    val isAvailable: Boolean,
    val timestamp: Long = 0L
)
