package com.rahulpahuja.waves.module.splash.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.rahulpahuja.waves.data.remote.FirestoreRepository
import com.rahulpahuja.waves.ui.navigation.Screen
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repository: FirestoreRepository
) : ViewModel() {

    private val _destination = MutableStateFlow<String?>(null)
    val destination: StateFlow<String?> = _destination.asStateFlow()

    fun checkAuthState() {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser
                if (currentUser == null) {
                    _destination.value = Screen.Login.route
                } else {
                    val user = repository.getUser(currentUser.uid)
                    if (user == null) {
                        _destination.value = Screen.RoleSelection.route
                    } else {
                        when (user.status) {
                            "APPROVED" -> {
                                _destination.value = if (user.role == "admin") {
                                    Screen.AdminDashboard.route
                                } else {
                                    Screen.Welcome.route // Or StudentDashboard
                                }
                            }
                            "PENDING" -> {
                                _destination.value = Screen.WaitingApproval.route
                            }
                            else -> {
                                _destination.value = Screen.Login.route
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("SplashViewModel", "Error checking auth state: ${e.message}", e)
                // If we can't check auth state (e.g. offline and no cache), 
                // we might want to still allow them to see the login screen or a dedicated offline screen.
                // For now, let's go to Login so they can see the error message there if they try to sign in.
                _destination.value = Screen.Login.route
            }
        }
    }
}
