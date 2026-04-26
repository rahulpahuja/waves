package com.rahulpahuja.waves.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahulpahuja.waves.data.local.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val dataStore: DataStoreManager
) : ViewModel() {

    private val _colorScheme = MutableStateFlow(AppTheme.ADMIN_SLATE.colorScheme())
    val colorScheme: StateFlow<ColorScheme> = _colorScheme.asStateFlow()

    private val _currentTheme = MutableStateFlow(AppTheme.ADMIN_SLATE)
    val currentTheme: StateFlow<AppTheme> = _currentTheme.asStateFlow()

    private val _userRole = MutableStateFlow("admin")
    val userRole: StateFlow<String> = _userRole.asStateFlow()

    init {
        viewModelScope.launch {
            combine(dataStore.selectedTheme, dataStore.userRole) { themeKey, role ->
                val resolvedRole = role ?: "admin"
                val theme = if (themeKey != null) AppTheme.fromKey(themeKey)
                            else AppTheme.defaultForRole(resolvedRole)
                Pair(theme, resolvedRole)
            }.collect { (theme, role) ->
                _currentTheme.value = theme
                _colorScheme.value = theme.colorScheme()
                _userRole.value = role
            }
        }
    }

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            dataStore.saveTheme(theme.name)
        }
    }
}
