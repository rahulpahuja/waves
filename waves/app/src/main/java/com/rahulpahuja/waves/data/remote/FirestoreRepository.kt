package com.rahulpahuja.waves.data.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "FirestoreRepository"

private const val COLLECTION_USERS = "users"
private const val COLLECTION_CHATS = "chats"
private const val COLLECTION_MESSAGES = "messages"
private const val COLLECTION_SETTINGS = "settings"
private const val COLLECTION_BOOKINGS = "bookings"
private const val COLLECTION_PAYMENTS = "payments"
private const val COLLECTION_PROGRESS = "progress"
private const val COLLECTION_COURSES = "courses"
private const val COLLECTION_NOTIFICATIONS = "notifications"
private const val COLLECTION_ENROLLMENTS = "enrollments"
private const val COLLECTION_ANNOUNCEMENTS = "announcements"

private const val DOCUMENT_STUDIO_CONFIG = "studio_config"

private const val FIELD_STATUS = "status"
private const val FIELD_EMAIL = "email"
private const val FIELD_TIMESTAMP = "timestamp"
private const val FIELD_START_TIME = "startTime"
private const val FIELD_USER_ID = "userId"
private const val FIELD_REJECTION_REASON = "rejectionReason"
private const val FIELD_VERIFIED_BY = "verifiedBy"
private const val FIELD_VERIFICATION_DATE = "verificationDate"
private const val FIELD_RECEIPT_ID = "receiptId"
private const val FIELD_MESSAGE = "message"
private const val FIELD_TYPE = "type"
private const val FIELD_READ = "read"
private const val FIELD_FCM_TOKEN = "fcmToken"

private const val RECEIPT_PREFIX = "REC"
private const val RECEIPT_MODULUS = 1_000_000
private const val DEFAULT_COURSE_CATEGORY = "DJing"
private const val DEFAULT_STUDIO_START_TIME = "09:00"
private const val DEFAULT_STUDIO_END_TIME = "17:00"
private const val DEFAULT_TIMEZONE = "IST"
private const val DEFAULT_STUDENT_LEVEL = "Beginner"
private const val ANNOUNCEMENTS_LIMIT = 20L

private const val LOG_ERROR_FETCH_USERS = "Error fetching users: %s"
private const val LOG_ERROR_SAVE_USER = "Error saving user (%s): %s"
private const val LOG_WARN_GET_USER_SERVER = "Failed to get user from server: %s. Trying cache..."
private const val LOG_ERROR_GET_USER_CACHE = "Failed to get user from cache: %s"
private const val LOG_ERROR_GET_USER_GENERAL = "General error getting user: %s"
private const val LOG_ERROR_FETCH_PENDING_USERS = "Error fetching pending users: %s"
private const val LOG_ERROR_UPDATE_USER_STATUS = "Error updating user status (%s): %s"
private const val LOG_ERROR_GET_USER_BY_EMAIL = "Error getting user by email (%s): %s"
private const val LOG_ERROR_FETCH_MESSAGES = "Error fetching messages: %s"
private const val LOG_ERROR_SEND_MESSAGE = "Error sending message to %s: %s"
private const val LOG_ERROR_FETCH_STUDIO_AVAILABILITY = "Error fetching studio availability: %s"
private const val LOG_ERROR_UPDATE_STUDIO_AVAILABILITY = "Error updating studio availability: %s"
private const val LOG_ERROR_FETCH_BOOKING_REQUESTS = "Error fetching booking requests: %s"
private const val LOG_ERROR_FETCH_USER_BOOKINGS = "Error fetching user bookings: %s"
private const val LOG_ERROR_CREATE_BOOKING_REQUEST = "Error creating booking request: %s"
private const val LOG_ERROR_UPDATE_BOOKING_STATUS = "Error updating booking status (%s): %s"
private const val LOG_ERROR_FETCH_PENDING_PAYMENTS = "Error fetching pending payments: %s"
private const val LOG_ERROR_VERIFY_PAYMENT = "Error verifying payment (%s): %s"
private const val LOG_ERROR_FETCH_STUDENT_PROGRESS = "Error fetching student progress: %s"
private const val LOG_ERROR_FETCH_COURSES = "Error fetching courses: %s"
private const val LOG_ERROR_SAVE_COURSE = "Error saving course: %s"
private const val LOG_ERROR_FETCH_USER_PAYMENTS = "Error fetching user payments: %s"
private const val LOG_ERROR_FETCH_ENROLLMENTS = "Error fetching enrollments: %s"
private const val LOG_ERROR_SAVE_ENROLLMENT = "Error saving enrollment: %s"
private const val LOG_ERROR_SAVE_FCM_TOKEN = "Error saving FCM token for %s: %s"
private const val LOG_ERROR_ADD_ANNOUNCEMENT = "Error adding announcement: %s"
private const val LOG_ERROR_FETCH_ANNOUNCEMENTS = "Error fetching announcements: %s"

enum class UserStatus(val value: String) {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED")
}

enum class PaymentStatus(val value: String) {
    PENDING("PENDING"),
    PAID("PAID"),
    OVERDUE("OVERDUE")
}

enum class BookingStatus(val value: String) {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED")
}

enum class BookingType(val value: String) {
    CHECKIN("CHECKIN"),
    BOOKING("BOOKING")
}

enum class CourseEnrollmentStatus(val value: String) {
    ACTIVE("ACTIVE"),
    COMPLETED("COMPLETED")
}

enum class NotificationType(val value: String) {
    PAYMENT_DUE("PAYMENT_DUE"),
    ABSENTEEISM("ABSENTEEISM"),
    GENERAL("GENERAL")
}

enum class MessageType(val value: String) {
    TEXT("text")
}

@Singleton
class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun getUsers(): Flow<List<FirestoreUser>> = callbackFlow {
        val subscription = firestore.collection(COLLECTION_USERS)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, LOG_ERROR_FETCH_USERS.format(error.message), error)
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
            firestore.collection(COLLECTION_USERS).document(user.uid).set(user).await()
        } catch (e: Exception) {
            Log.e(TAG, LOG_ERROR_SAVE_USER.format(user.uid, e.message))
            throw e
        }
    }

    suspend fun getUser(uid: String): FirestoreUser? {
        return try {
            // Default get() tries server, then cache if offline
            firestore.collection(COLLECTION_USERS).document(uid).get().await()
                .toObject(FirestoreUser::class.java)
        } catch (e: FirebaseFirestoreException) {
            Log.w(TAG, LOG_WARN_GET_USER_SERVER.format(e.message))
            try {
                // Force cache read if server fails/is offline
                firestore.collection(COLLECTION_USERS).document(uid).get(Source.CACHE).await()
                    .toObject(FirestoreUser::class.java)
            } catch (cacheEx: Exception) {
                Log.e(TAG, LOG_ERROR_GET_USER_CACHE.format(cacheEx.message))
                null // Return null instead of crashing
            }
        } catch (e: Exception) {
            Log.e(TAG, LOG_ERROR_GET_USER_GENERAL.format(e.message))
            null // Return null instead of crashing
        }
    }

    fun getPendingUsers(): Flow<List<FirestoreUser>> = callbackFlow {
        val subscription = firestore.collection(COLLECTION_USERS)
            .whereEqualTo(FIELD_STATUS, UserStatus.PENDING.value)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, LOG_ERROR_FETCH_PENDING_USERS.format(error.message), error)
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
            firestore.collection(COLLECTION_USERS).document(uid).update(FIELD_STATUS, status)
                .await()
        } catch (e: Exception) {
            Log.e(TAG, LOG_ERROR_UPDATE_USER_STATUS.format(uid, e.message))
        }
    }

    suspend fun getUserByEmail(email: String): FirestoreUser? {
        return try {
            firestore.collection(COLLECTION_USERS)
                .whereEqualTo(FIELD_EMAIL, email)
                .limit(1)
                .get()
                .await()
                .toObjects(FirestoreUser::class.java)
                .firstOrNull()
        } catch (e: Exception) {
            Log.e(TAG, LOG_ERROR_GET_USER_BY_EMAIL.format(email, e.message))
            null
        }
    }

    fun getMessages(chatId: String): Flow<List<FirestoreMessage>> = callbackFlow {
        val subscription = firestore.collection(COLLECTION_CHATS).document(chatId)
            .collection(COLLECTION_MESSAGES)
            .orderBy(FIELD_TIMESTAMP, Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, LOG_ERROR_FETCH_MESSAGES.format(error.message), error)
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
            firestore.collection(COLLECTION_CHATS).document(chatId)
                .collection(COLLECTION_MESSAGES).add(message).await()
        } catch (e: Exception) {
            Log.e(TAG, LOG_ERROR_SEND_MESSAGE.format(chatId, e.message))
        }
    }

    // --- Studio Management ---

    fun getStudioAvailability(): Flow<StudioAvailability?> = callbackFlow {
        val subscription =
            firestore.collection(COLLECTION_SETTINGS).document(DOCUMENT_STUDIO_CONFIG)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, LOG_ERROR_FETCH_STUDIO_AVAILABILITY.format(error.message), error)
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
            firestore.collection(COLLECTION_SETTINGS).document(DOCUMENT_STUDIO_CONFIG)
                .set(availability).await()
        } catch (e: Exception) {
            Log.e(TAG, LOG_ERROR_UPDATE_STUDIO_AVAILABILITY.format(e.message))
        }
    }

    fun getBookingRequests(): Flow<List<BookingRequest>> = callbackFlow {
        val subscription = firestore.collection(COLLECTION_BOOKINGS)
            .whereEqualTo(FIELD_STATUS, BookingStatus.PENDING.value)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, LOG_ERROR_FETCH_BOOKING_REQUESTS.format(error.message), error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val requests = snapshot?.toObjects(BookingRequest::class.java) ?: emptyList()
                trySend(requests)
            }
        awaitClose { subscription.remove() }
    }

    fun getUserBookings(uid: String): Flow<List<BookingRequest>> = callbackFlow {
        val subscription = firestore.collection(COLLECTION_BOOKINGS)
            .whereEqualTo(FIELD_USER_ID, uid)
            .orderBy(FIELD_START_TIME, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, LOG_ERROR_FETCH_USER_BOOKINGS.format(error.message), error)
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
            firestore.collection(COLLECTION_BOOKINGS).add(request).await()
        } catch (e: Exception) {
            Log.e(TAG, LOG_ERROR_CREATE_BOOKING_REQUEST.format(e.message))
        }
    }

    suspend fun updateBookingStatus(bookingId: String, status: String, rejectionReason: String? = null) {
        try {
            val updates = mutableMapOf<String, Any>(FIELD_STATUS to status)
            if (rejectionReason != null) updates[FIELD_REJECTION_REASON] = rejectionReason
            firestore.collection(COLLECTION_BOOKINGS).document(bookingId).update(updates).await()
        } catch (e: Exception) {
            Log.e(TAG, LOG_ERROR_UPDATE_BOOKING_STATUS.format(bookingId, e.message))
        }
    }

    // --- Payment Verification & Receipts ---

    fun getAllPendingPayments(): Flow<List<PaymentRecord>> = callbackFlow {
        val subscription = firestore.collection(COLLECTION_PAYMENTS)
            .whereEqualTo(FIELD_STATUS, PaymentStatus.PENDING.value)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, LOG_ERROR_FETCH_PENDING_PAYMENTS.format(error.message), error)
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
                FIELD_STATUS to PaymentStatus.PAID.value,
                FIELD_VERIFIED_BY to adminId,
                FIELD_VERIFICATION_DATE to System.currentTimeMillis(),
                FIELD_RECEIPT_ID to "$RECEIPT_PREFIX-${System.currentTimeMillis() % RECEIPT_MODULUS}"
            )
            firestore.collection(COLLECTION_PAYMENTS).document(paymentId).update(updates).await()
        } catch (e: Exception) {
            Log.e(TAG, LOG_ERROR_VERIFY_PAYMENT.format(paymentId, e.message))
        }
    }

    fun getStudentProgress(uid: String): Flow<StudentProgress?> = callbackFlow {
        val subscription = firestore.collection(COLLECTION_PROGRESS).document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, LOG_ERROR_FETCH_STUDENT_PROGRESS.format(error.message), error)
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
        val subscription = firestore.collection(COLLECTION_COURSES)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, LOG_ERROR_FETCH_COURSES.format(error.message), error)
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
            val courses = firestore.collection(COLLECTION_COURSES)
            val docRef =
                if (course.id.isEmpty()) courses.document() else courses.document(course.id)
            docRef.set(course.copy(id = docRef.id)).await()
        } catch (e: Exception) {
            Log.e(TAG, LOG_ERROR_SAVE_COURSE.format(e.message))
        }
    }

    fun getUserPayments(uid: String): Flow<List<PaymentRecord>> = callbackFlow {
        val subscription = firestore.collection(COLLECTION_PAYMENTS)
            .whereEqualTo(FIELD_USER_ID, uid)
            .orderBy(FIELD_TIMESTAMP, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, LOG_ERROR_FETCH_USER_PAYMENTS.format(error.message), error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val payments = snapshot?.toObjects(PaymentRecord::class.java) ?: emptyList()
                trySend(payments)
            }
        awaitClose { subscription.remove() }
    }

    suspend fun sendNotification(userId: String, message: String, type: NotificationType) {
        try {
            val notification = mapOf(
                FIELD_USER_ID to userId,
                FIELD_MESSAGE to message,
                FIELD_TYPE to type.value,
                FIELD_TIMESTAMP to System.currentTimeMillis(),
                FIELD_READ to false
            )
            firestore.collection(COLLECTION_NOTIFICATIONS).add(notification).await()
        } catch (e: Exception) {
            Log.e(TAG, "Error sending notification to $userId: ${e.message}")
        }
    }

    suspend fun sendPaymentNotification(userId: String, message: String) {
        sendNotification(userId, message, NotificationType.PAYMENT_DUE)
    }

    fun getUserEnrollments(uid: String): Flow<List<Enrollment>> = callbackFlow {
        val subscription = firestore.collection(COLLECTION_ENROLLMENTS)
            .whereEqualTo(FIELD_USER_ID, uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, LOG_ERROR_FETCH_ENROLLMENTS.format(error.message), error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObjects(Enrollment::class.java) ?: emptyList())
            }
        awaitClose { subscription.remove() }
    }

    suspend fun saveEnrollment(enrollment: Enrollment) {
        try {
            val enrollments = firestore.collection(COLLECTION_ENROLLMENTS)
            val docRef = if (enrollment.id.isEmpty()) {
                enrollments.document()
            } else {
                enrollments.document(enrollment.id)
            }
            docRef.set(enrollment.copy(id = docRef.id)).await()
        } catch (e: Exception) {
            Log.e(TAG, LOG_ERROR_SAVE_ENROLLMENT.format(e.message))
        }
    }

    suspend fun saveFcmToken(uid: String, token: String) {
        try {
            firestore.collection(COLLECTION_USERS).document(uid)
                .set(mapOf(FIELD_FCM_TOKEN to token), SetOptions.merge()).await()
        } catch (e: Exception) {
            Log.e(TAG, LOG_ERROR_SAVE_FCM_TOKEN.format(uid, e.message))
        }
    }

    suspend fun addAnnouncement(announcement: Announcement) {
        try {
            val docRef = firestore.collection(COLLECTION_ANNOUNCEMENTS).document()
            docRef.set(announcement.copy(id = docRef.id)).await()
        } catch (e: Exception) {
            Log.e(TAG, LOG_ERROR_ADD_ANNOUNCEMENT.format(e.message))
        }
    }

    fun getAnnouncements(): Flow<List<Announcement>> = callbackFlow {
        val subscription = firestore.collection(COLLECTION_ANNOUNCEMENTS)
            .orderBy(FIELD_TIMESTAMP, Query.Direction.DESCENDING)
            .limit(ANNOUNCEMENTS_LIMIT)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, LOG_ERROR_FETCH_ANNOUNCEMENTS.format(error.message), error)
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
    val category: String = DEFAULT_COURSE_CATEGORY
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
    val status: String = PaymentStatus.PENDING.value, // PENDING | PAID | OVERDUE
    val timestamp: Long = System.currentTimeMillis(),
    val dueDate: Long = 0L,
    val verifiedBy: String? = null,
    val verificationDate: Long? = null,
    val receiptId: String? = null
)

data class StudioAvailability(
    val startTime: String = DEFAULT_STUDIO_START_TIME,
    val endTime: String = DEFAULT_STUDIO_END_TIME,
    val timezone: String = DEFAULT_TIMEZONE,
    val isClosed: Boolean = false
)

data class BookingRequest(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val startTime: Long = 0L,
    val durationMinutes: Int = 60,
    val type: String = BookingType.BOOKING.value, // CHECKIN | BOOKING
    val status: String = BookingStatus.PENDING.value, // PENDING | APPROVED | REJECTED
    val rejectionReason: String? = null,
    val courseId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class StudentProgress(
    val userId: String = "",
    val currentLevel: String = DEFAULT_STUDENT_LEVEL,
    val completedSessions: Int = 0,
    val totalHours: Double = 0.0,
    val skillRatings: Map<String, Int> = emptyMap(), // e.g. {"Mixing": 4, "Beatmatching": 3}
    val courseHistory: List<CourseEnrollment> = emptyList()
)

data class CourseEnrollment(
    val courseId: String = "",
    val courseName: String = "",
    val enrollmentDate: Long = 0L,
    val status: String = CourseEnrollmentStatus.ACTIVE.value // ACTIVE | COMPLETED
)

data class FirestoreUser(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val role: String = "", // student | instructor | admin
    val status: String = UserStatus.PENDING.value // PENDING | APPROVED | REJECTED
)

data class FirestoreMessage(
    val senderId: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = MessageType.TEXT.value
)
