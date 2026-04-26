package com.rahulpahuja.waves.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
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
                    Log.e("FirestoreRepository", "Error fetching users: ${error.message}", error)
                    trySend(emptyList())
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
        try {
            firestore.collection("users").document(user.uid).set(user).await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error saving user (${user.uid}): ${e.message}")
            throw e
        }
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
                null // Return null instead of crashing
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "General error getting user: ${e.message}")
            null // Return null instead of crashing
        }
    }

    fun getPendingUsers(): Flow<List<FirestoreUser>> = callbackFlow {
        val subscription = firestore.collection("users")
            .whereEqualTo("status", "PENDING")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirestoreRepository", "Error fetching pending users: ${error.message}", error)
                    trySend(emptyList())
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
        try {
            firestore.collection("users").document(uid).update("status", status).await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error updating user status ($uid): ${e.message}")
        }
    }

    suspend fun getUserByEmail(email: String): FirestoreUser? {
        return try {
            firestore.collection("users")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .await()
                .toObjects(FirestoreUser::class.java)
                .firstOrNull()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting user by email ($email): ${e.message}")
            null
        }
    }

    fun getMessages(chatId: String): Flow<List<FirestoreMessage>> = callbackFlow {
        val subscription = firestore.collection("chats").document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirestoreRepository", "Error fetching messages: ${error.message}", error)
                    trySend(emptyList())
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
        try {
            firestore.collection("chats").document(chatId)
                .collection("messages").add(message).await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error sending message to $chatId: ${e.message}")
        }
    }

    // --- Studio Management ---

    fun getStudioAvailability(): Flow<StudioAvailability?> = callbackFlow {
        val subscription = firestore.collection("settings").document("studio_config")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirestoreRepository", "Error fetching studio availability: ${error.message}", error)
                    trySend(null)
                    return@addSnapshotListener
                }
                val config = snapshot?.toObject(StudioAvailability::class.java)
                trySend(config)
            }
        awaitClose { subscription.remove() }
    }

    suspend fun updateStudioAvailability(availability: StudioAvailability) {
        try {
            firestore.collection("settings").document("studio_config").set(availability).await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error updating studio availability: ${e.message}")
        }
    }

    fun getBookingRequests(): Flow<List<BookingRequest>> = callbackFlow {
        val subscription = firestore.collection("bookings")
            .whereEqualTo("status", "PENDING")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirestoreRepository", "Error fetching booking requests: ${error.message}", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val requests = snapshot?.toObjects(BookingRequest::class.java) ?: emptyList()
                trySend(requests)
            }
        awaitClose { subscription.remove() }
    }

    fun getUserBookings(uid: String): Flow<List<BookingRequest>> = callbackFlow {
        val subscription = firestore.collection("bookings")
            .whereEqualTo("userId", uid)
            .orderBy("startTime", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirestoreRepository", "Error fetching user bookings: ${error.message}", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val bookings = snapshot?.toObjects(BookingRequest::class.java) ?: emptyList()
                trySend(bookings)
            }
        awaitClose { subscription.remove() }
    }

    suspend fun createBookingRequest(request: BookingRequest) {
        try {
            firestore.collection("bookings").add(request).await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error creating booking request: ${e.message}")
        }
    }

    suspend fun updateBookingStatus(bookingId: String, status: String, rejectionReason: String? = null) {
        try {
            val updates = mutableMapOf<String, Any>("status" to status)
            if (rejectionReason != null) updates["rejectionReason"] = rejectionReason
            firestore.collection("bookings").document(bookingId).update(updates).await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error updating booking status ($bookingId): ${e.message}")
        }
    }

    // --- Payment Verification & Receipts ---

    fun getAllPendingPayments(): Flow<List<PaymentRecord>> = callbackFlow {
        val subscription = firestore.collection("payments")
            .whereEqualTo("status", "PENDING")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirestoreRepository", "Error fetching pending payments: ${error.message}", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val payments = snapshot?.toObjects(PaymentRecord::class.java) ?: emptyList()
                trySend(payments)
            }
        awaitClose { subscription.remove() }
    }

    suspend fun verifyPayment(paymentId: String, adminId: String) {
        try {
            val updates = mapOf(
                "status" to "PAID",
                "verifiedBy" to adminId,
                "verificationDate" to System.currentTimeMillis(),
                "receiptId" to "REC-${System.currentTimeMillis() % 1000000}"
            )
            firestore.collection("payments").document(paymentId).update(updates).await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error verifying payment ($paymentId): ${e.message}")
        }
    }

    fun getStudentProgress(uid: String): Flow<StudentProgress?> = callbackFlow {
        val subscription = firestore.collection("progress").document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirestoreRepository", "Error fetching student progress: ${error.message}", error)
                    trySend(null)
                    return@addSnapshotListener
                }
                val progress = snapshot?.toObject(StudentProgress::class.java)
                trySend(progress)
            }
        awaitClose { subscription.remove() }
    }

    // --- Course & Fee Management ---

    fun getCourses(): Flow<List<Course>> = callbackFlow {
        val subscription = firestore.collection("courses")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirestoreRepository", "Error fetching courses: ${error.message}", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val courses = snapshot?.toObjects(Course::class.java) ?: emptyList()
                trySend(courses)
            }
        awaitClose { subscription.remove() }
    }

    suspend fun saveCourse(course: Course) {
        try {
            val docRef = if (course.id.isEmpty()) firestore.collection("courses").document() else firestore.collection("courses").document(course.id)
            docRef.set(course.copy(id = docRef.id)).await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error saving course: ${e.message}")
        }
    }

    fun getUserPayments(uid: String): Flow<List<PaymentRecord>> = callbackFlow {
        val subscription = firestore.collection("payments")
            .whereEqualTo("userId", uid)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirestoreRepository", "Error fetching user payments: ${error.message}", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val payments = snapshot?.toObjects(PaymentRecord::class.java) ?: emptyList()
                trySend(payments)
            }
        awaitClose { subscription.remove() }
    }

    suspend fun sendPaymentNotification(userId: String, message: String) {
        try {
            val notification = mapOf(
                "userId" to userId,
                "message" to message,
                "type" to "PAYMENT_DUE",
                "timestamp" to System.currentTimeMillis(),
                "read" to false
            )
            firestore.collection("notifications").add(notification).await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error sending payment notification to $userId: ${e.message}")
        }
    }

    fun getUserEnrollments(uid: String): Flow<List<Enrollment>> = callbackFlow {
        val subscription = firestore.collection("enrollments")
            .whereEqualTo("userId", uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirestoreRepository", "Error fetching enrollments: ${error.message}", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObjects(Enrollment::class.java) ?: emptyList())
            }
        awaitClose { subscription.remove() }
    }

    suspend fun saveEnrollment(enrollment: Enrollment) {
        try {
            val docRef = if (enrollment.id.isEmpty()) firestore.collection("enrollments").document()
                         else firestore.collection("enrollments").document(enrollment.id)
            docRef.set(enrollment.copy(id = docRef.id)).await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error saving enrollment: ${e.message}")
        }
    }

    suspend fun saveFcmToken(uid: String, token: String) {
        try {
            firestore.collection("users").document(uid)
                .set(mapOf("fcmToken" to token), SetOptions.merge()).await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error saving FCM token for $uid: ${e.message}")
        }
    }

    suspend fun addAnnouncement(announcement: Announcement) {
        try {
            val docRef = firestore.collection("announcements").document()
            docRef.set(announcement.copy(id = docRef.id)).await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error adding announcement: ${e.message}")
        }
    }

    fun getAnnouncements(): Flow<List<Announcement>> = callbackFlow {
        val subscription = firestore.collection("announcements")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(20)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirestoreRepository", "Error fetching announcements: ${error.message}", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObjects(Announcement::class.java) ?: emptyList())
            }
        awaitClose { subscription.remove() }
    }
}

data class Course(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val fee: Double = 0.0,
    val durationWeeks: Int = 8,
    val topics: List<String> = emptyList(),
    val category: String = "DJing"
)

data class Enrollment(
    val id: String = "",
    val userId: String = "",
    val courseId: String = "",
    val courseName: String = "",
    val completedTopics: List<String> = emptyList(),
    val enrolledAt: Long = System.currentTimeMillis()
)

data class Announcement(
    val id: String = "",
    val title: String = "",
    val body: String = "",
    val courseId: String = "",
    val timestamp: Long = System.currentTimeMillis()
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
