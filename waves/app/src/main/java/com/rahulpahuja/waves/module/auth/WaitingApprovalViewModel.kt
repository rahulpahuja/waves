package com.rahulpahuja.waves.module.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rahulpahuja.waves.data.remote.FirestoreUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WaitingApprovalViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _userStatus = MutableStateFlow<String?>("PENDING")
    val userStatus: StateFlow<String?> = _userStatus.asStateFlow()

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole.asStateFlow()

    init {
        observeUserStatus()
    }

    private fun observeUserStatus() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("WaitingApprovalVM", "Error listening for status", error)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject(FirestoreUser::class.java)
                    _userStatus.value = user?.status
                    _userRole.value = user?.role
                }
            }
    }
}
