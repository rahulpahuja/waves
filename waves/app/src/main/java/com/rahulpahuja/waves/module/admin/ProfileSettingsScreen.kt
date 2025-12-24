package com.rahulpahuja.waves.module.admin

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
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahulpahuja.waves.module.student.SimpleTextField
import com.rahulpahuja.waves.module.student.NotificationToggleItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileSettingsViewModel = hiltViewModel()
) {
    val displayName by viewModel.displayName.collectAsState()
    val email by viewModel.email.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val bio by viewModel.bio.collectAsState()
    val faceIdLogin by viewModel.faceIdLogin.collectAsState()
    val newBookingRequests by viewModel.newBookingRequests.collectAsState()
    val lowAttendance by viewModel.lowAttendance.collectAsState()
    val marketingUpdates by viewModel.marketingUpdates.collectAsState()
    val autoApproveBookings by viewModel.autoApproveBookings.collectAsState()

    Scaffold(
        containerColor = Color(0xFF10141D),
        topBar = {
            TopAppBar(
                title = { 
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Profile Settings", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    TextButton(onClick = { /* Done/Save */ }) {
                        Text("Done", color = Color(0xFF2962FF), fontWeight = FontWeight.Bold)
                    }
                },
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
                Spacer(modifier = Modifier.height(8.dp))
                Text("Beat Academy App v2.4.1", color = Color.Gray, fontSize = 10.sp)
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
                            .background(Color.Gray.copy(alpha = 0.3f)), // Placeholder image
                        contentAlignment = Alignment.Center
                    ) {
                         // Image(painter = painterResource(id = R.drawable.profile_pic), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
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
                Text("Marcus 'DJ Spin' Vance", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Admin / Senior Instructor", color = Color.Gray, fontSize = 12.sp)
            }

            // Personal Info
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("PERSONAL INFO", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                
                SimpleTextField("Display Name", displayName) { viewModel.onDisplayNameChange(it) }
                SimpleTextField("Email", email) { viewModel.onEmailChange(it) }
                SimpleTextField("Phone", phone) { viewModel.onPhoneChange(it) }
                
                // Bio Field (Multi-line)
                Column {
                    Text("Bio", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = bio,
                        onValueChange = { viewModel.onBioChange(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
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

            // Security
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("SECURITY", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                
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

                 NotificationToggleItem("Face ID Login", null, faceIdLogin) { viewModel.onFaceIdLoginChange(it) }
            }

            // Notifications
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("NOTIFICATIONS", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                
                NotificationToggleItem("New Booking Requests", "Notify when a student books a slot", newBookingRequests) { viewModel.onNewBookingRequestsChange(it) }
                NotificationToggleItem("Low Attendance", "Alert if class is under 50% capacity", lowAttendance) { viewModel.onLowAttendanceChange(it) }
                NotificationToggleItem("Marketing Updates", "News about app features", marketingUpdates) { viewModel.onMarketingUpdatesChange(it) }
            }
            
            // School Defaults
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                 Row(
                     modifier = Modifier.fillMaxWidth(),
                     horizontalArrangement = Arrangement.SpaceBetween,
                     verticalAlignment = Alignment.CenterVertically
                 ) {
                     Text("SCHOOL DEFAULTS", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                     Box(
                         modifier = Modifier
                             .background(Color(0xFF2962FF), RoundedCornerShape(4.dp))
                             .padding(horizontal = 6.dp, vertical = 2.dp)
                     ) {
                         Text("ADMIN", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                     }
                 }

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
                        Text("Default Class Duration", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text("60 mins", color = Color.Gray, fontSize = 14.sp)
                        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.Gray)
                    }
                }
                
                NotificationToggleItem("Auto-Approve Bookings", null, autoApproveBookings) { viewModel.onAutoApproveBookingsChange(it) }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
