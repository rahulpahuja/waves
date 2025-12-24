package com.rahulpahuja.waves.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    fun signInAnonymously(onSuccess: () -> Unit, onError: (Exception?) -> Unit) {
        viewModelScope.launch {
            firebaseAuth.signInAnonymously()
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onError(it)
                }
        }
    }
}
