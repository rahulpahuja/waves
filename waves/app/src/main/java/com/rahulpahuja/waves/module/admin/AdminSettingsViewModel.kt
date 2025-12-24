package com.rahulpahuja.waves.module.admin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AdminSettingsViewModel @Inject constructor() : ViewModel() {

    // Preferences
    private val _darkMode = MutableStateFlow(true)
    val darkMode: StateFlow<Boolean> = _darkMode.asStateFlow()

    private val _language = MutableStateFlow("English")
    val language: StateFlow<String> = _language.asStateFlow()

    private val _notifications = MutableStateFlow(true)
    val notifications: StateFlow<Boolean> = _notifications.asStateFlow()

    fun onDarkModeChange(enabled: Boolean) { _darkMode.value = enabled }
    fun onLanguageChange(newValue: String) { _language.value = newValue }
    fun onNotificationsChange(enabled: Boolean) { _notifications.value = enabled }
}
