package com.rahulpahuja.waves.module.admin.usermanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahulpahuja.waves.data.remote.FirestoreRepository
import com.rahulpahuja.waves.data.remote.FirestoreUser
import com.rahulpahuja.waves.data.remote.NotificationType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserManagementViewModel @Inject constructor(
    private val repository: FirestoreRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<FirestoreUser>>(emptyList())
    val users: StateFlow<List<FirestoreUser>> = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getUsers().collect { userList ->
                _users.value = userList
                _isLoading.value = false
            }
        }
    }

    fun promoteToInstructor(user: FirestoreUser) {
        viewModelScope.launch {
            val updatedUser = user.copy(role = "instructor")
            repository.saveUser(updatedUser)
        }
    }

    fun notifyUser(userId: String, message: String, type: NotificationType) {
        viewModelScope.launch {
            repository.sendNotification(userId, message, type)
        }
    }
}
