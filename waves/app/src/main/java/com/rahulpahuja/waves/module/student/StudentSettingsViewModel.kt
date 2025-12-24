package com.rahulpahuja.waves.module.student

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StudentSettingsViewModel @Inject constructor() : ViewModel() {

    private val _fullName = MutableStateFlow("Alex Rivera")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _email = MutableStateFlow("alex.rivera@example.com")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _phone = MutableStateFlow("+1 (555) 012-3456")
    val phone: StateFlow<String> = _phone.asStateFlow()

    // Notification Toggles
    private val _pushNotifications = MutableStateFlow(true)
    val pushNotifications: StateFlow<Boolean> = _pushNotifications.asStateFlow()

    private val _classReminders = MutableStateFlow(true)
    val classReminders: StateFlow<Boolean> = _classReminders.asStateFlow()

    private val _feeDueAlerts = MutableStateFlow(true)
    val feeDueAlerts: StateFlow<Boolean> = _feeDueAlerts.asStateFlow()

    private val _announcements = MutableStateFlow(false)
    val announcements: StateFlow<Boolean> = _announcements.asStateFlow()

    fun onFullNameChange(newValue: String) { _fullName.value = newValue }
    fun onEmailChange(newValue: String) { _email.value = newValue }
    fun onPhoneChange(newValue: String) { _phone.value = newValue }

    fun onPushNotificationsChange(enabled: Boolean) { _pushNotifications.value = enabled }
    fun onClassRemindersChange(enabled: Boolean) { _classReminders.value = enabled }
    fun onFeeDueAlertsChange(enabled: Boolean) { _feeDueAlerts.value = enabled }
    fun onAnnouncementsChange(enabled: Boolean) { _announcements.value = enabled }
}
