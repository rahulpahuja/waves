package com.rahulpahuja.waves.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val isUnread: Boolean = false,
    val type: NotificationType,
    val icon: ImageVector? = null,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null
)

enum class NotificationType {
    CLASS,
    BOOKING,
    PAYMENT,
    MESSAGE,
    SYSTEM
}

@Composable
fun NotificationsScreen(
    onNavigateBack: () -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Classes", "Payments", "Studio")

    // Mock Data
    val newNotifications = listOf(
        NotificationItem(
            id = "1",
            title = "DJ Techniques 101",
            message = "Class starts in 1 hour in Room A. Don't forget your USB drive!",
            time = "1h ago",
            isUnread = true,
            type = NotificationType.CLASS
        ),
        NotificationItem(
            id = "2",
            title = "Studio B Confirmed",
            message = "Your booking for tomorrow at 2 PM is set.",
            time = "3h ago",
            isUnread = true,
            type = NotificationType.BOOKING
        )
    )

    val earlierNotifications = listOf(
        NotificationItem(
            id = "3",
            title = "Payment Successful",
            message = "Receipt for April Tuition has been sent to your email.",
            time = "Yesterday",
            type = NotificationType.PAYMENT
        ),
        NotificationItem(
            id = "4",
            title = "New Message",
            message = "Instructor Mike: \"Great work on the mix! I left some notes...\"",
            time = "2d ago",
            type = NotificationType.MESSAGE
        ),
        NotificationItem(
            id = "5",
            title = "System Maintenance",
            message = "School portal will be down for maintenance on Sunday 2AM-4AM.",
            time = "4d ago",
            type = NotificationType.SYSTEM
        )
    )

    Scaffold(
        containerColor = Color(0xFF10141D),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF10141D))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Notifications",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Mark all read",
                        color = Color(0xFF2962FF),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { /* Mark all read */ }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    filters.forEach { filter ->
                        FilterChip(
                            selected = filter == selectedFilter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2962FF),
                                selectedLabelColor = Color.White,
                                containerColor = Color(0xFF1E232F),
                                labelColor = Color.Gray
                            ),
                            border = null,
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "NEW",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(newNotifications) { notification ->
                NotificationItemView(notification)
            }

            item {
                Text(
                    text = "EARLIER",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }
            items(earlierNotifications) { notification ->
                NotificationItemView(notification)
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun NotificationItemView(notification: NotificationItem) {
    val icon = when (notification.type) {
        NotificationType.CLASS -> Icons.Default.GraphicEq // Waveform-like
        NotificationType.BOOKING -> Icons.Default.CalendarToday
        NotificationType.PAYMENT -> Icons.Default.Receipt
        NotificationType.MESSAGE -> Icons.Default.Person
        NotificationType.SYSTEM -> Icons.Default.Dns // Server icon approximation
    }

    val iconColor = when (notification.type) {
        NotificationType.CLASS -> Color(0xFF2962FF)
        NotificationType.BOOKING -> Color(0xFF9C27B0)
        NotificationType.PAYMENT -> Color(0xFF00E676)
        NotificationType.MESSAGE -> Color(0xFF2962FF)
        NotificationType.SYSTEM -> Color.Gray
    }
    
    val iconBgColor = iconColor.copy(alpha = 0.1f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(iconBgColor, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            // Small badge overlay for specific types if needed, e.g. time for Class
             if (notification.type == NotificationType.CLASS) {
                 Box(
                     modifier = Modifier
                         .align(Alignment.BottomEnd)
                         .offset(x = 4.dp, y = 4.dp)
                         .size(16.dp)
                         .background(Color(0xFF10141D), CircleShape)
                         .padding(2.dp)
                 ) {
                     Icon(
                         imageVector = Icons.Default.Schedule,
                         contentDescription = null,
                         tint = Color(0xFF2962FF),
                         modifier = Modifier.fillMaxSize()
                     )
                 }
             }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notification.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = notification.time,
                    color = if (notification.isUnread) Color(0xFF2962FF) else Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = if (notification.isUnread) FontWeight.Bold else FontWeight.Normal
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notification.message,
                color = Color.Gray,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
        
        if (notification.isUnread) {
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color(0xFF2962FF), CircleShape)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    NotificationsScreen()
}
