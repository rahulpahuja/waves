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
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val newUser = FirestoreUser(
                    uid = UUID.randomUUID().toString(), // Manual creation generates a random UID
                    email = email,
                    displayName = fullName,
                    role = role,
                    status = "APPROVED" // Manually created users are auto-approved
                )
                repository.saveUser(newUser)
                onSuccess()
            } catch (e: Exception) {
                // Log or handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
}
