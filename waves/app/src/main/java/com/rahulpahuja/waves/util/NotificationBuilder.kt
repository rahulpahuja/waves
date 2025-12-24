package com.rahulpahuja.waves.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.graphics.toColorInt
import com.rahulpahuja.waves.MainActivity
import com.rahulpahuja.waves.R
import java.util.Random

object NotificationBuilder {

    private const val CHANNEL_ID = "waves_notifications"
    private const val CHANNEL_NAME = "Waves Notifications"
    private const val CHANNEL_DESCRIPTION = "Notifications for Waves App"

    enum class NotificationType {
        CLASS_MISSED,
        BOOKING_CONFIRMED,
        FEE_DUE,
        ATTENDANCE_WARNING,
        COURSE_EXPIRY
    }

    fun showNotification(
        context: Context,
        type: NotificationType,
        title: String,
        message: String,
        bigText: String? = null
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(notificationManager)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Ensure this resource exists
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (bigText != null) {
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
        }

        // Add actions and styling based on type
        when (type) {
            NotificationType.CLASS_MISSED -> {
                builder.addAction(0, "View Session Details", pendingIntent)
                builder.setColor(Color.RED)
                // Note: setColorized(true) works for Foreground Services or specific styles, 
                // but for standard notifications it might only color the icon/accent depending on OS.
            }
            NotificationType.BOOKING_CONFIRMED -> {
                builder.addAction(0, "View Details", pendingIntent)
                builder.addAction(0, "Go to Calendar", pendingIntent)
                builder.setColor("#00E676".toColorInt())
            }
            NotificationType.FEE_DUE -> {
                // This usually should be an in-app dialog, but as a notification:
                builder.addAction(0, "View Payment History", pendingIntent)
                builder.setColor("#FFC107".toColorInt())
            }
            NotificationType.ATTENDANCE_WARNING -> {
                // This usually should be an in-app dialog
                builder.addAction(0, "View Attendance Record", pendingIntent)
                builder.setColor("#FF5722".toColorInt())
            }
            NotificationType.COURSE_EXPIRY -> {
                builder.addAction(0, "View Course Details", pendingIntent)
                builder.setColor("#9C27B0".toColorInt()) // Purple
            }
        }

        notificationManager.notify(Random().nextInt(), builder.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}
