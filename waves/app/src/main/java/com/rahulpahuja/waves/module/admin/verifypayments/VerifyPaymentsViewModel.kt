package com.rahulpahuja.waves.module.admin.verifypayments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.rahulpahuja.waves.data.remote.FirestoreRepository
import com.rahulpahuja.waves.data.remote.PaymentRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyPaymentsViewModel @Inject constructor(
    private val repository: FirestoreRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    val pendingPayments: StateFlow<List<PaymentRecord>> = repository.getAllPendingPayments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun verifyPayment(paymentId: String) {
        val adminId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repository.verifyPayment(paymentId, adminId)
        }
    }
}
