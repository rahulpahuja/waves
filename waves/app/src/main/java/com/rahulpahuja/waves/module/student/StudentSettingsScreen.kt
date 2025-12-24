package com.rahulpahuja.waves.module.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahulpahuja.waves.module.admin.CustomTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentSettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: StudentSettingsViewModel = hiltViewModel()
) {
    val fullName by viewModel.fullName.collectAsState()
    val email by viewModel.email.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val pushNotifications by viewModel.pushNotifications.collectAsState()
    val classReminders by viewModel.classReminders.collectAsState()
    val feeDueAlerts by viewModel.feeDueAlerts.collectAsState()
    val announcements by viewModel.announcements.collectAsState()

    StudentSettingsContent(
        fullName = fullName,
        email = email,
        phone = phone,
        pushNotifications = pushNotifications,
        classReminders = classReminders,
        feeDueAlerts = feeDueAlerts,
        announcements = announcements,
        onNavigateBack = onNavigateBack,
        onLogout = onLogout,
        onFullNameChange = { viewModel.onFullNameChange(it) },
        onEmailChange = { viewModel.onEmailChange(it) },
        onPhoneChange = { viewModel.onPhoneChange(it) },
        onPushNotificationsChange = { viewModel.onPushNotificationsChange(it) },
        onClassRemindersChange = { viewModel.onClassRemindersChange(it) },
        onFeeDueAlertsChange = { viewModel.onFeeDueAlertsChange(it) },
        onAnnouncementsChange = { viewModel.onAnnouncementsChange(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentSettingsContent(
    fullName: String,
    email: String,
    phone: String,
    pushNotifications: Boolean,
    classReminders: Boolean,
    feeDueAlerts: Boolean,
    announcements: Boolean,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPushNotificationsChange: (Boolean) -> Unit,
    onClassRemindersChange: (Boolean) -> Unit,
    onFeeDueAlertsChange: (Boolean) -> Unit,
    onAnnouncementsChange: (Boolean) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFF10141D),
        topBar = {
            TopAppBar(
                title = { 
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Settings", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = { Spacer(modifier = Modifier.width(48.dp)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF10141D))
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF10141D))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { /* Save Changes */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2962FF)),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("Save Changes", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onLogout) {
                    Text("Log Out", color = Color.Red, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Profile Header
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.Gray.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(64.dp))
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFF2962FF), CircleShape)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("DJ Mandy", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Student Member since 2023", color = Color.Gray, fontSize = 12.sp)
            }

            // Personal Information
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Personal Information", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                
                // Using existing CustomTextField or reimplementing simple version
                SimpleTextField("Full Name", fullName, onFullNameChange)
                SimpleTextField("Email Address", email, onEmailChange)
                SimpleTextField("Phone Number", phone, onPhoneChange)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E232F)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFF2962FF).copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Lock, contentDescription = null, tint = Color(0xFF2962FF), modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Change Password", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.Gray)
                    }
                }
            }

            // Artist Persona
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Artist Persona", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E232F)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Edit Artist Profile", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFF2962FF), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("PUBLIC", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Update bio, social links & genres", color = Color.Gray, fontSize = 12.sp)
                        }
                        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.Gray)
                    }
                }
            }

            // Notifications
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Notifications", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                
                NotificationToggleItem(
                    title = "Push Notifications",
                    subtitle = null,
                    checked = pushNotifications,
                    icon = Icons.Filled.Notifications,
                    onCheckedChange = onPushNotificationsChange
                )
                NotificationToggleItem(
                    title = "Class Reminders",
                    subtitle = "Alerts 1 hour before start",
                    checked = classReminders,
                    icon = Icons.Filled.DateRange,
                    onCheckedChange = onClassRemindersChange
                )
                NotificationToggleItem(
                    title = "Fee Due Alerts",
                    subtitle = null,
                    checked = feeDueAlerts,
                    icon = Icons.Filled.Payment,
                    onCheckedChange = onFeeDueAlertsChange
                )
                NotificationToggleItem(
                    title = "Announcements",
                    subtitle = "New courses and school news",
                    checked = announcements,
                    icon = Icons.Filled.Campaign,
                    onCheckedChange = onAnnouncementsChange
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SimpleTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(label, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1E232F)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1E232F),
                unfocusedContainerColor = Color(0xFF1E232F),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White
            )
        )
    }
}

@Composable
fun NotificationToggleItem(
    title: String,
    subtitle: String?,
    checked: Boolean,
    icon: ImageVector,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E232F)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            
            Box(
                modifier = Modifier
                    .size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                if (subtitle != null) {
                    Text(subtitle, color = Color.Gray, fontSize = 12.sp)
                }
            }
            
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF2962FF),
                    uncheckedThumbColor = Color.LightGray,
                    uncheckedTrackColor = Color.DarkGray,
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentSettingsScreenPreview() {
    StudentSettingsContent(
        fullName = "DJ Mandy",
        email = "dj.mandy@gmail.com",
        phone = "+1 555 012-3456",
        pushNotifications = true,
        classReminders = true,
        feeDueAlerts = true,
        announcements = false,
        onNavigateBack = {},
        onLogout = {},
        onFullNameChange = {},
        onEmailChange = {},
        onPhoneChange = {},
        onPushNotificationsChange = {},
        onClassRemindersChange = {},
        onFeeDueAlertsChange = {},
        onAnnouncementsChange = {}
    )
}
