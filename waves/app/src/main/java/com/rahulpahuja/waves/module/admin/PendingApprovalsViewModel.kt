package com.rahulpahuja.waves.module.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahulpahuja.waves.data.remote.FirestoreRepository
import com.rahulpahuja.waves.data.remote.FirestoreUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PendingApprovalsViewModel @Inject constructor(
    private val repository: FirestoreRepository
) : ViewModel() {

    val pendingUsers: StateFlow<List<FirestoreUser>> = repository.getPendingUsers()
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
