package com.rahulpahuja.waves.module.student.studentsettings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rahulpahuja.waves.ui.components.SettingsNavigationItem
import com.rahulpahuja.waves.ui.components.SettingsSection
import com.rahulpahuja.waves.ui.components.SettingsToggleItem
import com.rahulpahuja.waves.ui.theme.AppTheme
import com.rahulpahuja.waves.ui.theme.ThemePicker
import com.rahulpahuja.waves.ui.theme.WavesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentSettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    onAppMapClick: () -> Unit = {},
    viewModel: StudentSettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val fullName by viewModel.fullName.collectAsState()
    val email by viewModel.email.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val photoUrl by viewModel.photoUrl.collectAsState()
    val pushNotifications by viewModel.pushNotifications.collectAsState()
    val classReminders by viewModel.classReminders.collectAsState()
    val feeDueAlerts by viewModel.feeDueAlerts.collectAsState()
    val announcements by viewModel.announcements.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.onPhotoSelected(context, it) }
    }

    StudentSettingsContent(
        fullName = fullName,
        email = email,
        phone = phone,
        photoUrl = photoUrl,
        pushNotifications = pushNotifications,
        classReminders = classReminders,
        feeDueAlerts = feeDueAlerts,
        announcements = announcements,
        isLoading = isLoading,
        onNavigateBack = onNavigateBack,
        onLogout = onLogout,
        onSaveProfile = { viewModel.saveProfile() },
        onFullNameChange = { viewModel.onFullNameChange(it) },
        onEmailChange = { viewModel.onEmailChange(it) },
        onPhoneChange = { viewModel.onPhoneChange(it) },
        onPushNotificationsChange = { viewModel.onPushNotificationsChange(it) },
        onClassRemindersChange = { viewModel.onClassRemindersChange(it) },
        onFeeDueAlertsChange = { viewModel.onFeeDueAlertsChange(it) },
        onAnnouncementsChange = { viewModel.onAnnouncementsChange(it) },
        onPhotoClick = { photoPickerLauncher.launch("image/*") },
        onAppMapClick = onAppMapClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentSettingsContent(
    fullName: String,
    email: String,
    phone: String,
    photoUrl: String = "",
    pushNotifications: Boolean,
    classReminders: Boolean,
    feeDueAlerts: Boolean,
    announcements: Boolean,
    isLoading: Boolean = false,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    onSaveProfile: () -> Unit = {},
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPushNotificationsChange: (Boolean) -> Unit,
    onClassRemindersChange: (Boolean) -> Unit,
    onFeeDueAlertsChange: (Boolean) -> Unit,
    onAnnouncementsChange: (Boolean) -> Unit,
    onPhotoClick: () -> Unit,
    onAppMapClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { 
                    Text("Settings", color = Color.White, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = { 
                    TextButton(onClick = onSaveProfile) {
                        Text("Done", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Profile Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .clickable(onClick = onPhotoClick)
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    if (photoUrl.isNotEmpty()) {
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.Gray.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(64.dp))
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(fullName.ifEmpty { "Student" }, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Student", color = Color.Gray, fontSize = 12.sp)
            }

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Personal Information
                SettingsSection(title = "PERSONAL INFORMATION") {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            SimpleTextField("Full Name", fullName) { onFullNameChange(it) }
                            SimpleTextField("Email Address", email) { onEmailChange(it) }
                            SimpleTextField("Phone Number", phone) { onPhoneChange(it) }
                        }
                    }
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(start = 56.dp))
                    SettingsNavigationItem(
                        title = "Change Password",
                        icon = Icons.Filled.Lock,
                        iconColor = Color(0xFF2962FF),
                        onClick = { /* TODO */ }
                    )
                }

                // Artist Persona
                SettingsSection(title = "ARTIST PERSONA") {
                    SettingsNavigationItem(
                        title = "Edit Artist Profile",
                        subtitle = "Update bio, social links & genres",
                        value = "PUBLIC",
                        icon = Icons.Filled.Person,
                        iconColor = Color(0xFFFFD700),
                        onClick = { /* TODO */ }
                    )
                }

                // Notifications
                SettingsSection(title = "NOTIFICATIONS") {
                    SettingsToggleItem(
                        title = "Push Notifications",
                        checked = pushNotifications,
                        icon = Icons.Filled.Notifications,
                        iconColor = Color(0xFFE91E63),
                        onCheckedChange = onPushNotificationsChange
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(start = 56.dp))
                    SettingsToggleItem(
                        title = "Class Reminders",
                        subtitle = "Alerts 1 hour before start",
                        checked = classReminders,
                        icon = Icons.Filled.DateRange,
                        iconColor = Color(0xFF00E676),
                        onCheckedChange = onClassRemindersChange
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(start = 56.dp))
                    SettingsToggleItem(
                        title = "Fee Due Alerts",
                        checked = feeDueAlerts,
                        icon = Icons.Filled.Payment,
                        iconColor = Color(0xFFFFC107),
                        onCheckedChange = onFeeDueAlertsChange
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(start = 56.dp))
                    SettingsToggleItem(
                        title = "Announcements",
                        subtitle = "New courses and school news",
                        checked = announcements,
                        icon = Icons.Filled.Campaign,
                        iconColor = Color(0xFF9C27B0),
                        onCheckedChange = onAnnouncementsChange
                    )
                }

                // Appearance
                SettingsSection(title = "APPEARANCE") {
                    Box(modifier = Modifier.padding(vertical = 8.dp)) {
                        ThemePicker()
                    }
                }

                // Project Info
                SettingsSection(title = "PROJECT INFO") {
                    SettingsNavigationItem(
                        title = "App Architecture Map",
                        icon = Icons.Filled.Map,
                        iconColor = Color(0xFFFFD700),
                        onClick = onAppMapClick
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onSaveProfile,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Save Changes", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                TextButton(
                    onClick = onLogout,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Log Out", color = Color.Red, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
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
                .background(MaterialTheme.colorScheme.surface),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1E232F),
                unfocusedContainerColor = Color(0xFF1E232F),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StudentSettingsScreenPreview() {
    WavesTheme(colorScheme = AppTheme.STUDENT_OCEAN.colorScheme()) {
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
            onAnnouncementsChange = {},
            onPhotoClick = {}
        )
    }
}
