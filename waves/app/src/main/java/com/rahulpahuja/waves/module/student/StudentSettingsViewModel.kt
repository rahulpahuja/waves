package com.rahulpahuja.waves.module.student

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.rahulpahuja.waves.data.remote.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentSettingsViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repository: FirestoreRepository
) : ViewModel() {

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone.asStateFlow()

    private val _photoUrl = MutableStateFlow("")
    val photoUrl: StateFlow<String> = _photoUrl.asStateFlow()

    private val _pushNotifications = MutableStateFlow(true)
    val pushNotifications: StateFlow<Boolean> = _pushNotifications.asStateFlow()

    private val _classReminders = MutableStateFlow(true)
    val classReminders: StateFlow<Boolean> = _classReminders.asStateFlow()

    private val _feeDueAlerts = MutableStateFlow(true)
    val feeDueAlerts: StateFlow<Boolean> = _feeDueAlerts.asStateFlow()

    private val _announcements = MutableStateFlow(false)
    val announcements: StateFlow<Boolean> = _announcements.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser ?: return
        viewModelScope.launch {
            try {
                val user = repository.getUser(currentUser.uid)
                user?.let {
                    _fullName.value = it.displayName
                    _email.value = it.email
                    _photoUrl.value = it.photoUrl
                }
            } catch (e: Exception) {
                Log.e("StudentSettingsVM", "Error loading profile", e)
            }
        }
    }

    fun onFullNameChange(newValue: String) { _fullName.value = newValue }
    fun onEmailChange(newValue: String) { _email.value = newValue }
    fun onPhoneChange(newValue: String) { _phone.value = newValue }

    fun onPushNotificationsChange(enabled: Boolean) { _pushNotifications.value = enabled }
    fun onClassRemindersChange(enabled: Boolean) { _classReminders.value = enabled }
    fun onFeeDueAlertsChange(enabled: Boolean) { _feeDueAlerts.value = enabled }
    fun onAnnouncementsChange(enabled: Boolean) { _announcements.value = enabled }
}
