package com.rahulpahuja.waves.module.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestoreException
import com.rahulpahuja.waves.data.remote.FirestoreRepository
import com.rahulpahuja.waves.data.remote.FirestoreUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

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
        val emailValue = _email.value.trim().lowercase()
        val passwordValue = _password.value.trim()

        if (emailValue.isEmpty() || passwordValue.isEmpty()) {
            _authState.value = AuthState.Error("Please enter email and password")
            viewModelScope.launch { _toastEvent.emit("Please enter email and password") }
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            if (emailValue == SUPERADMIN_EMAIL) {
                // If password is empty, try with the default one
                val finalPassword = if (passwordValue.isEmpty()) SUPERADMIN_PASSWORD else passwordValue
                loginAsSuperAdmin(emailValue, finalPassword)
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
            Log.d("LoginViewModel", "SuperAdmin login attempt: $email")
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
            Log.d("LoginViewModel", "Superadmin signed in successfully")
            _toastEvent.emit("Superadmin access granted")
            _authState.value = AuthState.Success(isAdmin = true)
        } catch (e: Exception) {
            Log.w("LoginViewModel", "Superadmin initial sign-in failed, checking if creation is needed: ${e.message}")

            // If sign-in failed, it could be because:
            // 1. Account doesn't exist (need to create)
            // 2. Wrong password
            // 3. Different provider (e.g. Google)
            
            try {
                // Attempt to create the account. If it already exists, this will throw FirebaseAuthUserCollisionException
                Log.d("LoginViewModel", "Attempting to create superadmin account...")
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid ?: run {
                    _authState.value = AuthState.Error("Account creation failed")
                    return
                }
                val superAdmin = FirestoreUser(uid = uid, email = email, displayName = "Super Admin", role = "admin", status = "APPROVED")
                repository.saveUser(superAdmin)
                Log.d("LoginViewModel", "Superadmin account created and approved")
                _toastEvent.emit("Superadmin account initialized")
                _authState.value = AuthState.Success(isAdmin = true)
            } catch (createEx: Exception) {
                if ((createEx is FirebaseAuthUserCollisionException) || (createEx.message?.contains("already in use") == true)) {
                    Log.e("LoginViewModel", "Superadmin collision: Account exists")
                    val msg = "Invalid password. This email is already registered."
                    _toastEvent.emit(msg)
                    _authState.value = AuthState.Error(msg)
                } else {
                    Log.e("LoginViewModel", "Superadmin create failed", createEx)
                    val msg = createEx.message ?: "Failed to authenticate superadmin"
                    _toastEvent.emit(msg)
                    _authState.value = AuthState.Error(msg)
                }
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
                Log.d("LoginViewModel", "User authenticated but no Firestore record found. Creating profile...")
                currentUserInfo = FirestoreUser(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: email,
                    displayName = firebaseUser.displayName ?: "New User"
                )
                _authState.value = AuthState.NeedsRoleSelection
            } else {
                handleExistingUser(existingUser)
            }
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Login error", e)
            val errorMessage = when (e) {
                is FirebaseAuthInvalidUserException -> {
                    // This error occurs if the email is not registered.
                    // Instead of just failing, let's offer to create an account by trying to sign up.
                    _toastEvent.emit("Email not found. Creating new account...")
                    signUpNewUser(email, password)
                    return
                }
                is FirebaseAuthInvalidCredentialsException -> "Invalid email or password."
                else -> {
                    if (e.message?.contains("operation is not allowed", ignoreCase = true) == true) {
                        "Email/Password login is not enabled in Firebase. Please enable it in the Firebase Console."
                    } else {
                        e.message ?: "Login failed"
                    }
                }
            }
            _toastEvent.emit(errorMessage)
            _authState.value = AuthState.Error(errorMessage)
        }
    }

    private suspend fun signUpNewUser(email: String, password: String) {
        try {
            Log.d("LoginViewModel", "Email not found, attempting to sign up: $email")
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: run {
                _authState.value = AuthState.Error("Sign up failed: user is null")
                return
            }
            Log.d("LoginViewModel", "Sign up Success: ${firebaseUser.uid}")
            _toastEvent.emit("Welcome back!")
            currentUserInfo = FirestoreUser(
                uid = firebaseUser.uid, 
                email = firebaseUser.email ?: email, 
                displayName = "New User"
            )
            _authState.value = AuthState.NeedsRoleSelection
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Sign up error", e)
            val errorMessage = when {
                e is FirebaseAuthUserCollisionException -> "This email is already registered with a different method."
                e.message?.contains("password", ignoreCase = true) == true -> "Password is too weak or invalid."
                else -> e.message ?: "Sign up failed"
            }
            _toastEvent.emit(errorMessage)
            _authState.value = AuthState.Error(errorMessage)
        }
    }

    companion object {
        private const val SUPERADMIN_EMAIL = "superadmin@waves.com"
        private const val SUPERADMIN_PASSWORD = "admin123"
    }

    fun logout(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                auth.signOut()
                _authState.value = AuthState.Idle
                currentUserInfo = null
                _email.value = ""
                _password.value = ""
                _toastEvent.emit("Logged out")
                onComplete()
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Logout failed", e)
                _toastEvent.emit("Logout failed")
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
                        _toastEvent.emit("Account created with Google")
                        
                        // SUPER ADMIN BYPASS: If email is the designated superadmin, approve immediately
                        val isSuperAdmin = firebaseUser.email == SUPERADMIN_EMAIL
                        
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
                            _toastEvent.emit("Superadmin access granted via Google")
                            repository.saveUser(currentUserInfo!!)
                            _authState.value = AuthState.Success(isAdmin = true)
                        } else {
                            // Explicitly set state to trigger navigation in LoginScreen
                            _authState.value = AuthState.NeedsRoleSelection
                        }
                    } else {
                        Log.d("LoginViewModel", "Existing user found, checking status: ${existingUser.status}")
                        _toastEvent.emit("Welcome back, ${existingUser.displayName}")
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
                    e.message?.contains("collision", ignoreCase = true) == true || 
                    e.message?.contains("already-in-use", ignoreCase = true) == true -> {
                        "This email is already linked to a different login method (e.g. Password vs Google). Please use the original method you signed up with."
                    }
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
                _toastEvent.emit(errorMessage)
                _authState.value = AuthState.Error(errorMessage)
            }
        }
    }

    private fun handleExistingUser(user: FirestoreUser) {
        when (user.status) {
            "APPROVED" -> {
                viewModelScope.launch { _toastEvent.emit("Login successful") }
                _authState.value = AuthState.Success(isAdmin = user.role == "admin")
            }
            "PENDING" -> {
                viewModelScope.launch { _toastEvent.emit("Account pending approval") }
                _authState.value = AuthState.PendingApproval
            }
            "REJECTED" -> {
                val msg = "Your account has been rejected."
                viewModelScope.launch { _toastEvent.emit(msg) }
                _authState.value = AuthState.Error(msg)
            }
            else -> {
                val msg = "Invalid account status."
                viewModelScope.launch { _toastEvent.emit(msg) }
                _authState.value = AuthState.Error(msg)
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
            val msg = "Session expired. Please log in again."
            viewModelScope.launch { _toastEvent.emit(msg) }
            _authState.value = AuthState.Error(msg)
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                Log.d("LoginViewModel", "Saving user to repository: $user")
                repository.saveUser(user)
                Log.d("LoginViewModel", "User saved successfully, updating authState to PendingApproval")
                _toastEvent.emit("Profile submitted for approval")
                _authState.value = AuthState.PendingApproval
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Failed to save user role: ${e.message}", e)
                val msg = e.message ?: "Failed to save user role"
                _toastEvent.emit(msg)
                _authState.value = AuthState.Error(msg)
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
