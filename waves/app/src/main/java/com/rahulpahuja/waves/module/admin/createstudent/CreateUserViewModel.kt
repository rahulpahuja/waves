package com.rahulpahuja.waves.module.admin.createstudent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahulpahuja.waves.data.remote.FirestoreRepository
import com.rahulpahuja.waves.data.remote.FirestoreUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateUserViewModel @Inject constructor(
    private val repository: FirestoreRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun createUser(
        fullName: String,
        email: String,
        role: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Check if user already exists
                val existing = repository.getUserByEmail(email.trim().lowercase())
                if (existing != null) {
                    onError("A user with this email already exists.")
                    return@launch
                }

                val newUser = FirestoreUser(
                    uid = UUID.randomUUID().toString(),
                    email = email.trim().lowercase(),
                    displayName = fullName,
                    role = role,
                    status = "APPROVED"
                )
                repository.saveUser(newUser)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to create user")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
