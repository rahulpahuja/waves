package com.rahulpahuja.waves.service

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rahulpahuja.waves.data.remote.FirestoreRepository
import com.rahulpahuja.waves.util.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject lateinit var repository: FirestoreRepository
    @Inject lateinit var auth: FirebaseAuth

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "FCM token refreshed")
        val uid = auth.currentUser?.uid ?: return
        CoroutineScope(Dispatchers.IO).launch {
            try { repository.saveFcmToken(uid, token) }
            catch (e: Exception) { Log.e(TAG, "Failed to save FCM token", e) }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val title = remoteMessage.notification?.title
            ?: remoteMessage.data["title"]
            ?: "Waves"
        val body = remoteMessage.notification?.body
            ?: remoteMessage.data["body"]
            ?: return
        NotificationHelper.sendNotification(this, title, body)
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}
