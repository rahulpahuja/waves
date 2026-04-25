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

    suspend fun getUserByEmail(email: String): FirestoreUser? {
        return firestore.collection("users")
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .await()
            .toObjects(FirestoreUser::class.java)
            .firstOrNull()
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

    // --- Studio Management ---

    fun getStudioAvailability(): Flow<StudioAvailability?> = callbackFlow {
        val subscription = firestore.collection("settings").document("studio_config")
            .addSnapshotListener { snapshot, _ ->
                val config = snapshot?.toObject(StudioAvailability::class.java)
                trySend(config)
            }
        awaitClose { subscription.remove() }
    }

    suspend fun updateStudioAvailability(availability: StudioAvailability) {
        firestore.collection("settings").document("studio_config").set(availability).await()
    }

    fun getBookingRequests(): Flow<List<BookingRequest>> = callbackFlow {
        val subscription = firestore.collection("bookings")
            .whereEqualTo("status", "PENDING")
            .addSnapshotListener { snapshot, _ ->
                val requests = snapshot?.toObjects(BookingRequest::class.java) ?: emptyList()
                trySend(requests)
            }
        awaitClose { subscription.remove() }
    }

    fun getUserBookings(uid: String): Flow<List<BookingRequest>> = callbackFlow {
        val subscription = firestore.collection("bookings")
            .whereEqualTo("userId", uid)
            .orderBy("startTime", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                val bookings = snapshot?.toObjects(BookingRequest::class.java) ?: emptyList()
                trySend(bookings)
            }
        awaitClose { subscription.remove() }
    }

    suspend fun createBookingRequest(request: BookingRequest) {
        firestore.collection("bookings").add(request).await()
    }

    suspend fun updateBookingStatus(bookingId: String, status: String, rejectionReason: String? = null) {
        val updates = mutableMapOf<String, Any>("status" to status)
        if (rejectionReason != null) updates["rejectionReason"] = rejectionReason
        firestore.collection("bookings").document(bookingId).update(updates).await()
    }

    // --- Payment Verification & Receipts ---

    fun getAllPendingPayments(): Flow<List<PaymentRecord>> = callbackFlow {
        val subscription = firestore.collection("payments")
            .whereEqualTo("status", "PENDING")
            .addSnapshotListener { snapshot, _ ->
                val payments = snapshot?.toObjects(PaymentRecord::class.java) ?: emptyList()
                trySend(payments)
            }
        awaitClose { subscription.remove() }
    }

    suspend fun verifyPayment(paymentId: String, adminId: String) {
        val updates = mapOf(
            "status" to "PAID",
            "verifiedBy" to adminId,
            "verificationDate" to System.currentTimeMillis(),
            "receiptId" to "REC-${System.currentTimeMillis() % 1000000}"
        )
        firestore.collection("payments").document(paymentId).update(updates).await()
    }

    fun getStudentProgress(uid: String): Flow<StudentProgress?> = callbackFlow {
        val subscription = firestore.collection("progress").document(uid)
            .addSnapshotListener { snapshot, _ ->
                val progress = snapshot?.toObject(StudentProgress::class.java)
                trySend(progress)
            }
        awaitClose { subscription.remove() }
    }

    // --- Course & Fee Management ---

    fun getCourses(): Flow<List<Course>> = callbackFlow {
        val subscription = firestore.collection("courses")
            .addSnapshotListener { snapshot, _ ->
                val courses = snapshot?.toObjects(Course::class.java) ?: emptyList()
                trySend(courses)
            }
        awaitClose { subscription.remove() }
    }

    suspend fun saveCourse(course: Course) {
        val docRef = if (course.id.isEmpty()) firestore.collection("courses").document() else firestore.collection("courses").document(course.id)
        docRef.set(course.copy(id = docRef.id)).await()
    }

    fun getUserPayments(uid: String): Flow<List<PaymentRecord>> = callbackFlow {
        val subscription = firestore.collection("payments")
            .whereEqualTo("userId", uid)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                val payments = snapshot?.toObjects(PaymentRecord::class.java) ?: emptyList()
                trySend(payments)
            }
        awaitClose { subscription.remove() }
    }

    suspend fun sendPaymentNotification(userId: String, message: String) {
        val notification = mapOf(
            "userId" to userId,
            "message" to message,
            "type" to "PAYMENT_DUE",
            "timestamp" to System.currentTimeMillis(),
            "read" to false
        )
        firestore.collection("notifications").add(notification).await()
    }
}

data class Course(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val fee: Double = 0.0,
    val durationWeeks: Int = 8
)

data class PaymentRecord(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val courseId: String = "",
    val courseName: String = "",
    val amount: Double = 0.0,
    val status: String = "PENDING", // PENDING | PAID | OVERDUE
    val timestamp: Long = System.currentTimeMillis(),
    val dueDate: Long = 0L,
    val verifiedBy: String? = null,
    val verificationDate: Long? = null,
    val receiptId: String? = null
)

data class StudioAvailability(
    val startTime: String = "09:00",
    val endTime: String = "17:00",
    val timezone: String = "IST",
    val isClosed: Boolean = false
)

data class BookingRequest(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val startTime: Long = 0L,
    val durationMinutes: Int = 60,
    val type: String = "BOOKING", // CHECKIN | BOOKING
    val status: String = "PENDING", // PENDING | APPROVED | REJECTED
    val rejectionReason: String? = null,
    val courseId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class StudentProgress(
    val userId: String = "",
    val currentLevel: String = "Beginner",
    val completedSessions: Int = 0,
    val totalHours: Double = 0.0,
    val skillRatings: Map<String, Int> = emptyMap(), // e.g. {"Mixing": 4, "Beatmatching": 3}
    val courseHistory: List<CourseEnrollment> = emptyList()
)

data class CourseEnrollment(
    val courseId: String = "",
    val courseName: String = "",
    val enrollmentDate: Long = 0L,
    val status: String = "ACTIVE" // ACTIVE | COMPLETED
)

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
