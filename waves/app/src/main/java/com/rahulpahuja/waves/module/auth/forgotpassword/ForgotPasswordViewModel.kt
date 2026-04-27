package com.rahulpahuja.waves.module.auth.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _uiState = MutableStateFlow<ForgotPasswordUiState>(ForgotPasswordUiState.Idle)
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    fun onEmailChange(newValue: String) {
        _email.value = newValue
    }

    fun onResetPasswordClick() {
        val emailValue = _email.value.trim()
        if (emailValue.isEmpty()) {
            viewModelScope.launch { _toastEvent.emit("Please enter your email address") }
            return
        }

        viewModelScope.launch {
            _uiState.value = ForgotPasswordUiState.Loading
            try {
                auth.sendPasswordResetEmail(emailValue).await()
                _uiState.value = ForgotPasswordUiState.Success
                _toastEvent.emit("Password reset email sent to $emailValue")
            } catch (e: Exception) {
                _uiState.value = ForgotPasswordUiState.Error(e.message ?: "Failed to send reset email")
                _toastEvent.emit(e.message ?: "Failed to send reset email")
            }
        }
    }
}

sealed class ForgotPasswordUiState {
    object Idle : ForgotPasswordUiState()
    object Loading : ForgotPasswordUiState()
    object Success : ForgotPasswordUiState()
    data class Error(val message: String) : ForgotPasswordUiState()
}
