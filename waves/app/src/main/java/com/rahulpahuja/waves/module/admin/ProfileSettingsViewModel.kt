package com.rahulpahuja.waves.module.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.rahulpahuja.waves.data.remote.FirestoreRepository
import com.rahulpahuja.waves.data.remote.FirestoreUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class ProfileSettingsViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repository: FirestoreRepository
) : ViewModel() {

    private val _displayName = MutableStateFlow("")
    val displayName: StateFlow<String> = _displayName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _photoUrl = MutableStateFlow("")
    val photoUrl: StateFlow<String> = _photoUrl.asStateFlow()

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone.asStateFlow()

    private val _bio = MutableStateFlow("")
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

    private val _role = MutableStateFlow("")
    val role: StateFlow<String> = _role.asStateFlow()

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = repository.getUser(currentUser.uid)
                user?.let {
                    _displayName.value = it.displayName
                    _email.value = it.email
                    _photoUrl.value = it.photoUrl
                    _role.value = it.role
                    _status.value = it.status
                    // Note: phone and bio are not in the current FirestoreUser data class, 
                    // but you might want to add them there later.
                }
            } catch (e: Exception) {
                Log.e("ProfileSettingsVM", "Error loading profile", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveProfile() {
        val currentUser = auth.currentUser ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val updatedUser = FirestoreUser(
                    uid = currentUser.uid,
                    displayName = _displayName.value,
                    email = _email.value,
                    photoUrl = _photoUrl.value,
                    role = _role.value,
                    status = _status.value
                )
                repository.saveUser(updatedUser)
                Log.d("ProfileSettingsVM", "Profile updated successfully")
            } catch (e: Exception) {
                Log.e("ProfileSettingsVM", "Error saving profile", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

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
