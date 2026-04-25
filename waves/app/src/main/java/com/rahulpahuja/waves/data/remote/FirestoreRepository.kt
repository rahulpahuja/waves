package com.rahulpahuja.waves.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.FirebaseFirestoreException
import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun getUsers(): Flow<List<FirestoreUser>> = callbackFlow {
        val subscription = firestore.collection("users")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val users = snapshot.toObjects(FirestoreUser::class.java)
                    trySend(users)
                }
            }
        awaitClose { subscription.remove() }
    }

    suspend fun saveUser(user: FirestoreUser) {
        firestore.collection("users").document(user.uid).set(user).await()
    }

    suspend fun getUser(uid: String): FirestoreUser? {
        return try {
            // Default get() tries server, then cache if offline
            firestore.collection("users").document(uid).get().await().toObject(FirestoreUser::class.java)
        } catch (e: FirebaseFirestoreException) {
            Log.w("FirestoreRepository", "Failed to get user from server: ${e.message}. Trying cache...")
            try {
                // Force cache read if server fails/is offline
                firestore.collection("users").document(uid).get(Source.CACHE).await().toObject(FirestoreUser::class.java)
            } catch (cacheEx: Exception) {
                Log.e("FirestoreRepository", "Failed to get user from cache: ${cacheEx.message}")
                throw e // Rethrow original exception if cache also fails
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "General error getting user: ${e.message}")
            throw e
        }
    }

    fun getPendingUsers(): Flow<List<FirestoreUser>> = callbackFlow {
        val subscription = firestore.collection("users")
            .whereEqualTo("status", "PENDING")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val users = snapshot.toObjects(FirestoreUser::class.java)
                    trySend(users)
                }
            }
        awaitClose { subscription.remove() }
    }

    suspend fun updateUserStatus(uid: String, status: String) {
        firestore.collection("users").document(uid).update("status", status).await()
    }

    fun getMessages(chatId: String): Flow<List<FirestoreMessage>> = callbackFlow {
        val subscription = firestore.collection("chats").document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val messages = snapshot.toObjects(FirestoreMessage::class.java)
                    trySend(messages)
                }
            }
        awaitClose { subscription.remove() }
    }

    suspend fun sendMessage(chatId: String, message: FirestoreMessage) {
        firestore.collection("chats").document(chatId)
            .collection("messages").add(message).await()
    }
}

data class FirestoreUser(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val role: String = "", // student | admin
    val status: String = "PENDING" // PENDING | APPROVED | REJECTED
)

data class FirestoreMessage(
    val senderId: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "text"
)
