package com.rahulpahuja.waves.module.admin.pendingapprovals

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahulpahuja.waves.data.remote.FirestoreRepository
import com.rahulpahuja.waves.data.remote.FirestoreUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PendingApprovalsViewModel @Inject constructor(
    private val repository: FirestoreRepository
) : ViewModel() {

    val pendingUsers: StateFlow<List<FirestoreUser>> = repository.getPendingUsers()
        .catch { e -> 
            Log.e("PendingApprovalsVM", "Error loading pending users: ${e.message}", e)
            emit(emptyList()) 
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun approveUser(uid: String) {
        viewModelScope.launch {
            repository.updateUserStatus(uid, "APPROVED")
        }
    }

    fun rejectUser(uid: String) {
        viewModelScope.launch {
            repository.updateUserStatus(uid, "REJECTED")
        }
    }
}
