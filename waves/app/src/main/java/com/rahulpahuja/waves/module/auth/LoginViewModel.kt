package com.rahulpahuja.waves.module.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
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

    fun onLoginClick() {
        val emailValue = _email.value
        val passwordValue = _password.value

        if (emailValue.isEmpty() || passwordValue.isEmpty()) {
            _authState.value = AuthState.Error("Please enter email and password")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            if (emailValue == SUPERADMIN_EMAIL) {
                loginAsSuperAdmin(emailValue, passwordValue)
            } else {
                loginWithEmailPassword(emailValue, passwordValue)
            }
        }
    }

    // Three-stage superadmin login:
    // 1. Try email/password sign-in (succeeds if account was previously set up with a password)
    // 2. If account not found → create it fresh with the given password
    // 3. If account exists but linked to a different provider (e.g. Google) → bypass Firebase Auth
    //    and verify via Firestore instead, then grant admin access directly
    private suspend fun loginAsSuperAdmin(email: String, password: String) {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: run {
                _authState.value = AuthState.Error("Login failed: user is null")
                return
            }
            val existing = repository.getUser(uid)
            if (existing == null) {
                val superAdmin = FirestoreUser(uid = uid, email = email, displayName = "Super Admin", role = "admin", status = "APPROVED")
                repository.saveUser(superAdmin)
            }
            Log.d("LoginViewModel", "Superadmin signed in via email/password")
            _authState.value = AuthState.Success(isAdmin = true)
        } catch (e: FirebaseAuthInvalidUserException) {
            // Account does not exist at all → create it
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid ?: run {
                    _authState.value = AuthState.Error("Account creation failed")
                    return
                }
                val superAdmin = FirestoreUser(uid = uid, email = email, displayName = "Super Admin", role = "admin", status = "APPROVED")
                repository.saveUser(superAdmin)
                Log.d("LoginViewModel", "Superadmin account created")
                _authState.value = AuthState.Success(isAdmin = true)
            } catch (createEx: Exception) {
                Log.e("LoginViewModel", "Superadmin create failed", createEx)
                _authState.value = AuthState.Error(createEx.message ?: "Failed to create superadmin account")
            }
        } catch (e: Exception) {
            // Account exists but email/password auth fails (wrong password, different provider, etc.)
            // Fall back to Firestore verification — the password is not validated in this path.
            Log.w("LoginViewModel", "Superadmin Firebase auth failed (${e.message}), attempting Firestore bypass")
            try {
                val superAdmin = repository.getUserByEmail(email)
                if (superAdmin != null && superAdmin.role == "admin") {
                    Log.d("LoginViewModel", "Superadmin verified via Firestore bypass")
                    _authState.value = AuthState.Success(isAdmin = true)
                } else {
                    // Not in Firestore yet — create Firestore record and grant access
                    // (Firebase Auth account exists, just not linked to email/password)
                    val newRecord = FirestoreUser(uid = "superadmin", email = email, displayName = "Super Admin", role = "admin", status = "APPROVED")
                    repository.saveUser(newRecord)
                    Log.d("LoginViewModel", "Superadmin Firestore record created via bypass")
                    _authState.value = AuthState.Success(isAdmin = true)
                }
            } catch (firestoreEx: Exception) {
                Log.e("LoginViewModel", "Superadmin Firestore bypass failed", firestoreEx)
                _authState.value = AuthState.Error("Unable to authenticate superadmin. Check your connection.")
            }
        }
    }

    private suspend fun loginWithEmailPassword(email: String, password: String) {
        try {
            Log.d("LoginViewModel", "Attempting email login for: $email")
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: run {
                _authState.value = AuthState.Error("Login failed: user is null")
                return
            }
            Log.d("LoginViewModel", "Email Auth Success: ${firebaseUser.uid}")
            val existingUser = repository.getUser(firebaseUser.uid)
            if (existingUser == null) {
                currentUserInfo = FirestoreUser(uid = firebaseUser.uid, email = firebaseUser.email ?: "", displayName = "New User")
                _authState.value = AuthState.NeedsRoleSelection
            } else {
                handleExistingUser(existingUser)
            }
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Login error", e)
            val errorMessage = when {
                e.message?.contains("operation is not allowed", ignoreCase = true) == true ->
                    "Email/Password login is not enabled in Firebase. Please enable it in the Firebase Console."
                else -> e.message ?: "Login failed"
            }
            _authState.value = AuthState.Error(errorMessage)
        }
    }

    companion object {
        private const val SUPERADMIN_EMAIL = "superadmin@waves.com"
    }

    fun logout(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                auth.signOut()
                _authState.value = AuthState.Idle
                currentUserInfo = null
                _email.value = ""
                _password.value = ""
                onComplete()
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Logout failed", e)
            }
        }
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
                        
                        // SUPER ADMIN BYPASS: If email is the designated superadmin, approve immediately
                        val isSuperAdmin = firebaseUser.email == "superadmin@waves.com"
                        
                        currentUserInfo = FirestoreUser(
                            uid = firebaseUser.uid,
                            email = firebaseUser.email ?: "",
                            displayName = firebaseUser.displayName ?: "",
                            photoUrl = firebaseUser.photoUrl?.toString() ?: "",
                            role = if (isSuperAdmin) "admin" else "",
                            status = if (isSuperAdmin) "APPROVED" else "PENDING"
                        )
                        
                        if (isSuperAdmin) {
                            Log.d("LoginViewModel", "Super Admin detected, auto-approving...")
                            repository.saveUser(currentUserInfo!!)
                            _authState.value = AuthState.Success(isAdmin = true)
                        } else {
                            // Explicitly set state to trigger navigation in LoginScreen
                            _authState.value = AuthState.NeedsRoleSelection
                        }
                    } else {
                        Log.d("LoginViewModel", "Existing user found, checking status: ${existingUser.status}")
                        val refreshedUser = existingUser.copy(
                            displayName = firebaseUser.displayName?.takeIf { it.isNotEmpty() } ?: existingUser.displayName,
                            photoUrl = firebaseUser.photoUrl?.toString()?.takeIf { it.isNotEmpty() } ?: existingUser.photoUrl,
                            email = firebaseUser.email?.takeIf { it.isNotEmpty() } ?: existingUser.email
                        )
                        if (refreshedUser != existingUser) {
                            repository.saveUser(refreshedUser)
                        }
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
        Log.d("LoginViewModel", "selectRole called with: $role")
        
        // Ensure we have user info, if not try to get it from current firebase user
        if (currentUserInfo == null) {
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                currentUserInfo = FirestoreUser(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: "",
                    photoUrl = firebaseUser.photoUrl?.toString() ?: ""
                )
            }
        }

        val user = currentUserInfo?.copy(role = role, status = "PENDING") ?: run {
            Log.e("LoginViewModel", "selectRole failed: currentUserInfo is null and no firebase user")
            _authState.value = AuthState.Error("Session expired. Please log in again.")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                Log.d("LoginViewModel", "Saving user to repository: $user")
                repository.saveUser(user)
                Log.d("LoginViewModel", "User saved successfully, updating authState to PendingApproval")
                _authState.value = AuthState.PendingApproval
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Failed to save user role: ${e.message}", e)
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
