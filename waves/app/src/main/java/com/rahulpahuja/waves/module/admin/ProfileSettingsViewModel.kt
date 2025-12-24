package com.rahulpahuja.waves.module.admin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileSettingsViewModel @Inject constructor() : ViewModel() {

    private val _displayName = MutableStateFlow("DJ Spin")
    val displayName: StateFlow<String> = _displayName.asStateFlow()

    private val _email = MutableStateFlow("marcus@beatacademy.com")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _phone = MutableStateFlow("+1 (555) 019-2834")
    val phone: StateFlow<String> = _phone.asStateFlow()

    private val _bio = MutableStateFlow("15 years of experience in scratching and beat matching. Teaching the next generation of vinyl enthusiasts.")
    val bio: StateFlow<String> = _bio.asStateFlow()

    // Security
    private val _faceIdLogin = MutableStateFlow(true)
    val faceIdLogin: StateFlow<Boolean> = _faceIdLogin.asStateFlow()

    // Notification Toggles
    private val _newBookingRequests = MutableStateFlow(true)
    val newBookingRequests: StateFlow<Boolean> = _newBookingRequests.asStateFlow()

    private val _lowAttendance = MutableStateFlow(true)
    val lowAttendance: StateFlow<Boolean> = _lowAttendance.asStateFlow()

    private val _marketingUpdates = MutableStateFlow(false)
    val marketingUpdates: StateFlow<Boolean> = _marketingUpdates.asStateFlow()

    // School Defaults
    private val _autoApproveBookings = MutableStateFlow(false)
    val autoApproveBookings: StateFlow<Boolean> = _autoApproveBookings.asStateFlow()

    fun onDisplayNameChange(newValue: String) { _displayName.value = newValue }
    fun onEmailChange(newValue: String) { _email.value = newValue }
    fun onPhoneChange(newValue: String) { _phone.value = newValue }
    fun onBioChange(newValue: String) { _bio.value = newValue }

    fun onFaceIdLoginChange(enabled: Boolean) { _faceIdLogin.value = enabled }
    fun onNewBookingRequestsChange(enabled: Boolean) { _newBookingRequests.value = enabled }
    fun onLowAttendanceChange(enabled: Boolean) { _lowAttendance.value = enabled }
    fun onMarketingUpdatesChange(enabled: Boolean) { _marketingUpdates.value = enabled }
    fun onAutoApproveBookingsChange(enabled: Boolean) { _autoApproveBookings.value = enabled }
}
