package com.rahulpahuja.waves.module.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestoreException
import com.rahulpahuja.waves.data.remote.FirestoreRepository
import com.rahulpahuja.waves.data.remote.FirestoreUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repository: FirestoreRepository
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private var currentUserInfo: FirestoreUser? = null

    fun onEmailChange(newValue: String) {
        _email.value = newValue
    }

    fun onPasswordChange(newValue: String) {
        _password.value = newValue
    }

    fun onPasswordVisibilityToggle() {
        _passwordVisible.value = !_passwordVisible.value
    }

    fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: AuthCredential) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                Log.d("LoginViewModel", "Signing in with Firebase credential...")
                val result = auth.signInWithCredential(credential).await()
                val firebaseUser = result.user
                if (firebaseUser != null) {
                    Log.d("LoginViewModel", "Firebase Auth Success: ${firebaseUser.uid}")
                    val existingUser = repository.getUser(firebaseUser.uid)
                    if (existingUser == null) {
                        Log.d("LoginViewModel", "New user detected, redirecting to role selection")
                        currentUserInfo = FirestoreUser(
                            uid = firebaseUser.uid,
                            email = firebaseUser.email ?: "",
                            displayName = firebaseUser.displayName ?: "",
                            photoUrl = firebaseUser.photoUrl?.toString() ?: ""
                        )
                        _authState.value = AuthState.NeedsRoleSelection
                    } else {
                        Log.d("LoginViewModel", "Existing user found, checking status: ${existingUser.status}")
                        handleExistingUser(existingUser)
                    }
                } else {
                    Log.e("LoginViewModel", "Firebase Auth failed: user is null")
                    _authState.value = AuthState.Error("Login failed: User is null")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Firebase Auth Exception: ${e.message}", e)
                val errorMessage = when {
                    e is FirebaseFirestoreException && e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED -> {
                        "Cloud Firestore API is not enabled. Please enable it in the Firebase Console."
                    }
                    e is FirebaseFirestoreException && e.code == FirebaseFirestoreException.Code.UNAVAILABLE -> {
                        "You seem to be offline. Please check your internet connection."
                    }
                    e.message?.contains("offline", ignoreCase = true) == true -> {
                        "You seem to be offline. Please check your internet connection."
                    }
                    else -> e.message ?: "An unknown error occurred"
                }
                _authState.value = AuthState.Error(errorMessage)
            }
        }
    }

    private fun handleExistingUser(user: FirestoreUser) {
        when (user.status) {
            "APPROVED" -> {
                _authState.value = AuthState.Success(isAdmin = user.role == "admin")
            }
            "PENDING" -> {
                _authState.value = AuthState.PendingApproval
            }
            "REJECTED" -> {
                _authState.value = AuthState.Error("Your account has been rejected.")
            }
            else -> {
                _authState.value = AuthState.Error("Invalid account status.")
            }
        }
    }

    fun selectRole(role: String) {
        val user = currentUserInfo?.copy(role = role, status = "PENDING") ?: return
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                repository.saveUser(user)
                _authState.value = AuthState.PendingApproval
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Failed to save user role")
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object NeedsRoleSelection : AuthState()
    object PendingApproval : AuthState()
    data class Success(val isAdmin: Boolean) : AuthState()
    data class Error(val message: String) : AuthState()
}
