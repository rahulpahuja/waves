package com.rahulpahuja.waves.module.admin.profilesettings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rahulpahuja.waves.module.student.studentsettings.SimpleTextField
import com.rahulpahuja.waves.ui.components.SettingsNavigationItem
import com.rahulpahuja.waves.ui.components.SettingsSection
import com.rahulpahuja.waves.ui.components.SettingsToggleItem
import com.rahulpahuja.waves.ui.theme.AppTheme
import com.rahulpahuja.waves.ui.theme.WavesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileSettingsViewModel = hiltViewModel()
) {
    val displayName by viewModel.displayName.collectAsState()
    val email by viewModel.email.collectAsState()
    val photoUrl by viewModel.photoUrl.collectAsState()
    val role by viewModel.role.collectAsState()
    val status by viewModel.status.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val bio by viewModel.bio.collectAsState()
    val faceIdLogin by viewModel.faceIdLogin.collectAsState()
    val newBookingRequests by viewModel.newBookingRequests.collectAsState()
    val lowAttendance by viewModel.lowAttendance.collectAsState()
    val marketingUpdates by viewModel.marketingUpdates.collectAsState()
    val autoApproveBookings by viewModel.autoApproveBookings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    ProfileSettingsContent(
        displayName = displayName,
        email = email,
        photoUrl = photoUrl,
        role = role,
        status = status,
        phone = phone,
        bio = bio,
        faceIdLogin = faceIdLogin,
        newBookingRequests = newBookingRequests,
        lowAttendance = lowAttendance,
        marketingUpdates = marketingUpdates,
        autoApproveBookings = autoApproveBookings,
        isLoading = isLoading,
        onNavigateBack = onNavigateBack,
        onLogout = onLogout,
        onSaveProfile = { viewModel.saveProfile() },
        onDisplayNameChange = { viewModel.onDisplayNameChange(it) },
        onEmailChange = { viewModel.onEmailChange(it) },
        onPhoneChange = { viewModel.onPhoneChange(it) },
        onBioChange = { viewModel.onBioChange(it) },
        onFaceIdLoginChange = { viewModel.onFaceIdLoginChange(it) },
        onNewBookingRequestsChange = { viewModel.onNewBookingRequestsChange(it) },
        onLowAttendanceChange = { viewModel.onLowAttendanceChange(it) },
        onMarketingUpdatesChange = { viewModel.onMarketingUpdatesChange(it) },
        onAutoApproveBookingsChange = { viewModel.onAutoApproveBookingsChange(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsContent(
    displayName: String,
    email: String,
    photoUrl: String,
    role: String,
    status: String,
    phone: String,
    bio: String,
    faceIdLogin: Boolean,
    newBookingRequests: Boolean,
    lowAttendance: Boolean,
    marketingUpdates: Boolean,
    autoApproveBookings: Boolean,
    isLoading: Boolean,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    onSaveProfile: () -> Unit,
    onDisplayNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onFaceIdLoginChange: (Boolean) -> Unit,
    onNewBookingRequestsChange: (Boolean) -> Unit,
    onLowAttendanceChange: (Boolean) -> Unit,
    onMarketingUpdatesChange: (Boolean) -> Unit,
    onAutoApproveBookingsChange: (Boolean) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { 
                    Text("Profile Settings", color = Color.White, fontWeight = FontWeight.Bold)
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
                modifier = Modifier.padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
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
                Text(displayName, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("${role.replaceFirstChar { it.uppercase() }} / $status", color = Color.Gray, fontSize = 12.sp)
            }

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Personal Info
                SettingsSection(title = "PERSONAL INFO") {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            SimpleTextField("Display Name", displayName) { onDisplayNameChange(it) }
                            SimpleTextField("Email", email) { onEmailChange(it) }
                            SimpleTextField("Phone", phone) { onPhoneChange(it) }
                            
                            // Bio Field (Multi-line)
                            Column {
                                Text("Bio", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = bio,
                                    onValueChange = onBioChange,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
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
                    }
                }

                // Security
                SettingsSection(title = "SECURITY") {
                    SettingsNavigationItem(
                        title = "Change Password",
                        icon = Icons.Filled.Lock,
                        iconColor = Color(0xFF2962FF),
                        onClick = { /* TODO */ }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(start = 56.dp))
                    SettingsToggleItem(
                        title = "Face ID Login",
                        checked = faceIdLogin,
                        icon = Icons.Filled.Face,
                        iconColor = Color(0xFF00E676),
                        onCheckedChange = onFaceIdLoginChange
                    )
                }

                // Notifications
                SettingsSection(title = "NOTIFICATIONS") {
                    SettingsToggleItem(
                        title = "New Booking Requests",
                        subtitle = "Notify when a student books a slot",
                        checked = newBookingRequests,
                        icon = Icons.Filled.DateRange,
                        iconColor = Color(0xFFE91E63),
                        onCheckedChange = onNewBookingRequestsChange
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(start = 56.dp))
                    SettingsToggleItem(
                        title = "Low Attendance",
                        subtitle = "Alert if class is under 50% capacity",
                        checked = lowAttendance,
                        icon = Icons.Filled.Warning,
                        iconColor = Color(0xFFFFC107),
                        onCheckedChange = onLowAttendanceChange
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(start = 56.dp))
                    SettingsToggleItem(
                        title = "Marketing Updates",
                        subtitle = "News about app features",
                        checked = marketingUpdates,
                        icon = Icons.Filled.Campaign,
                        iconColor = Color(0xFF9C27B0),
                        onCheckedChange = onMarketingUpdatesChange
                    )
                }
                
                // School Defaults
                SettingsSection(title = "SCHOOL DEFAULTS") {
                    SettingsNavigationItem(
                        title = "Default Class Duration",
                        value = "60 mins",
                        icon = Icons.Filled.DateRange, // Placeholder icon
                        iconColor = Color(0xFF03A9F4),
                        onClick = { /* TODO */ }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(start = 56.dp))
                    SettingsToggleItem(
                        title = "Auto-Approve Bookings",
                        checked = autoApproveBookings,
                        icon = Icons.Filled.CheckCircle,
                        iconColor = Color(0xFF4CAF50),
                        onCheckedChange = onAutoApproveBookingsChange
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
                
                Text(
                    text = "Beat Academy App v2.4.1",
                    color = Color.Gray,
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileSettingsScreenPreview() {
    WavesTheme(colorScheme = AppTheme.ADMIN_SLATE.colorScheme()) {
        ProfileSettingsContent(
            displayName = "Marcus Vance",
            email = "marcus@waves.com",
            photoUrl = "",
            role = "admin",
            status = "APPROVED",
            phone = "+1 555 0123",
            bio = "Senior Instructor at Beat Academy.",
            faceIdLogin = true,
            newBookingRequests = true,
            lowAttendance = false,
            marketingUpdates = true,
            autoApproveBookings = false,
            isLoading = false,
            onNavigateBack = {},
            onLogout = {},
            onSaveProfile = {},
            onDisplayNameChange = {},
            onEmailChange = {},
            onPhoneChange = {},
            onBioChange = {},
            onFaceIdLoginChange = {},
            onNewBookingRequestsChange = {},
            onLowAttendanceChange = {},
            onMarketingUpdatesChange = {},
            onAutoApproveBookingsChange = {}
        )
    }
}
