package com.rahulpahuja.waves.ui.navigation.map

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rahulpahuja.waves.ui.navigation.Screen
import com.rahulpahuja.waves.ui.navigation.ScreenGroup
import com.rahulpahuja.waves.ui.theme.AppTheme
import com.rahulpahuja.waves.ui.theme.WavesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMapScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Waves Academy: App Map", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            MapHeader()

            MapGroupSection(
                title = "1. Authentication & Security",
                icon = Icons.Default.Lock,
                group = ScreenGroup.AUTH,
                description = "Entry point and security validation layer."
            )

            MapGroupSection(
                title = "2. Onboarding Flow",
                icon = Icons.Default.AccountTree,
                group = ScreenGroup.ONBOARDING,
                description = "User persona definition and setup."
            )

            MapGroupSection(
                title = "3. Admin Dashboard (Staff)",
                icon = Icons.Default.Settings,
                group = ScreenGroup.ADMIN,
                description = "Management tools for courses, users, and financials."
            )

            MapGroupSection(
                title = "4. Student Experience",
                icon = Icons.Default.Person,
                group = ScreenGroup.STUDENT,
                description = "Personal learning dashboard and progress tracking."
            )

            MapGroupSection(
                title = "5. Shared Ecosystem",
                icon = Icons.Default.Share,
                group = ScreenGroup.SHARED,
                description = "Common features like Chat, Gallery, and Radar."
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun MapHeader() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2962FF).copy(alpha = 0.1f)),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2962FF).copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Map, contentDescription = null, tint = Color(0xFF2962FF), modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Project Architecture", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Comprehensive visual map of routes and logic groups.", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun MapGroupSection(
    title: String,
    icon: ImageVector,
    group: ScreenGroup,
    description: String
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(description, color = Color.Gray, fontSize = 11.sp)
        Spacer(modifier = Modifier.height(12.dp))
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Find screens belonging to this group
            // In a real scenario we'd use reflection or a registry, but here we list key ones
            val screens = listOf(
                Screen.Splash, Screen.Login, Screen.ForgotPassword, Screen.RoleSelection, Screen.WaitingApproval, Screen.PendingApprovals,
                Screen.Welcome, Screen.TrackProgress, Screen.CreatePersona, Screen.AllSet, Screen.WelcomeToTheBooth, Screen.ManageStudentLifecycle, Screen.ProfileSetup, Screen.AdminWelcome,
                Screen.AdminDashboard, Screen.AdminSettings, Screen.Students, Screen.CreateStudent, Screen.NewCashEntry, Screen.CreateSession, Screen.ManageBookings, Screen.HelpSupport, Screen.Attendance, Screen.ProfileSettings, Screen.ManageCourses, Screen.VerifyPayments, Screen.UserManagement,
                Screen.StudentDashboard, Screen.StudentSettings, Screen.PaymentHistory, Screen.ArtistProfile,
                Screen.Home, Screen.Notifications, Screen.Chat, Screen.MediaGallery, Screen.Settings, Screen.ArtistRadar, Screen.PublicArtistProfile, Screen.StudioSchedule, Screen.AppMap
            ).filter { it.group == group }

            screens.forEach { screen ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        screen::class.java.simpleName,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "/${screen.route}",
                        color = Color(0xFF00E676).copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppMapScreenPreview() {
    WavesTheme(colorScheme = AppTheme.ADMIN_SLATE.colorScheme()) {
        AppMapScreen(onNavigateBack = {})
    }
}
