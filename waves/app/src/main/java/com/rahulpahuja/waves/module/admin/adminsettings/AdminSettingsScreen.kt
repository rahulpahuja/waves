package com.rahulpahuja.waves.module.admin.adminsettings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReportProblem
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
import com.rahulpahuja.waves.ui.components.SettingsNavigationItem
import com.rahulpahuja.waves.ui.components.SettingsSection
import com.rahulpahuja.waves.ui.components.SettingsToggleItem
import com.rahulpahuja.waves.ui.theme.AppTheme
import com.rahulpahuja.waves.ui.theme.ThemePicker
import com.rahulpahuja.waves.ui.theme.ThemePickerContent
import com.rahulpahuja.waves.ui.theme.WavesTheme

@Composable
fun AdminSettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    onAppMapClick: () -> Unit,
    viewModel: AdminSettingsViewModel = hiltViewModel(),
    onArtistProfileClick: () -> Unit
) {
    val darkMode by viewModel.darkMode.collectAsState()
    val language by viewModel.language.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    AdminSettingsContent(
        darkMode = darkMode,
        language = language,
        notifications = notifications,
        onNavigateBack = onNavigateBack,
        onLogout = onLogout,
        onAppMapClick = onAppMapClick,
        onDarkModeChange = { viewModel.onDarkModeChange(it) },
        onNotificationsChange = { viewModel.onNotificationsChange(it) },
        onArtistProfileClick = onArtistProfileClick,
        themePicker = { ThemePicker() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSettingsContent(
    darkMode: Boolean,
    language: String,
    notifications: Boolean,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    onAppMapClick: () -> Unit,
    onDarkModeChange: (Boolean) -> Unit,
    onNotificationsChange: (Boolean) -> Unit,
    onArtistProfileClick: () -> Unit,
    themePicker: @Composable () -> Unit
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
            ProfileHeader(onArtistProfileClick)

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Appearance Section
                SettingsSection(title = "APPEARANCE") {
                    Box(modifier = Modifier.padding(vertical = 8.dp)) {
                        themePicker()
                    }
                }

                // Preferences Section
                SettingsSection(title = "PREFERENCES") {
                    SettingsToggleItem(
                        title = "Dark Mode",
                        icon = Icons.Filled.DarkMode,
                        iconColor = Color(0xFF2962FF),
                        checked = darkMode,
                        onCheckedChange = onDarkModeChange
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(start = 56.dp))
                    SettingsNavigationItem(
                        title = "Language",
                        value = language,
                        icon = Icons.Filled.Language,
                        iconColor = Color(0xFF00E676),
                        onClick = { /* TODO */ }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(start = 56.dp))
                    SettingsToggleItem(
                        title = "Notifications",
                        icon = Icons.Filled.Notifications,
                        iconColor = Color(0xFFE91E63),
                        checked = notifications,
                        onCheckedChange = onNotificationsChange
                    )
                }

                // Project Info Section
                SettingsSection(title = "PROJECT INFO") {
                    SettingsNavigationItem(
                        title = "App Architecture Map",
                        icon = Icons.Default.Map,
                        iconColor = Color(0xFFFFD700),
                        onClick = onAppMapClick
                    )
                }

                // Support Section
                SettingsSection(title = "SUPPORT") {
                    SettingsNavigationItem(
                        title = "Help Center",
                        icon = Icons.Filled.Help,
                        iconColor = Color(0xFF2962FF),
                        onClick = { /* TODO */ }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(start = 56.dp))
                    SettingsNavigationItem(
                        title = "Report a Bug",
                        icon = Icons.Filled.ReportProblem,
                        iconColor = Color(0xFFFFC107),
                        onClick = { /* TODO */ }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(start = 56.dp))
                    SettingsNavigationItem(
                        title = "Contact Us",
                        icon = Icons.Filled.Email,
                        iconColor = Color(0xFF9C27B0),
                        onClick = { /* TODO */ }
                    )
                }

                // Account Section
                SettingsSection(title = "ACCOUNT") {
                    SettingsNavigationItem(
                        title = "Log Out",
                        icon = Icons.Filled.Logout,
                        iconColor = Color(0xFF607D8B),
                        onClick = onLogout,
                        showChevron = false
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(start = 56.dp))
                    SettingsNavigationItem(
                        title = "Delete Account",
                        icon = Icons.Filled.Delete,
                        iconColor = Color.Red,
                        onClick = { /* TODO */ },
                        showChevron = false,
                        titleColor = Color.Red
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Version 2.4.1 (Build 8902)",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ProfileHeader(onArtistProfileClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0C9A6)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Admin User",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "admin@waves.com",
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onArtistProfileClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Edit Profile", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AdminSettingsPreview() {
    WavesTheme(colorScheme = AppTheme.ADMIN_SLATE.colorScheme()) {
        AdminSettingsContent(
            darkMode = true,
            language = "English",
            notifications = true,
            onNavigateBack = {},
            onLogout = {},
            onAppMapClick = {},
            onDarkModeChange = {},
            onNotificationsChange = {},
            onArtistProfileClick = {},
            themePicker = {
                ThemePickerContent(
                    themes = AppTheme.forRole("admin"),
                    currentTheme = AppTheme.ADMIN_SLATE,
                    onThemeSelected = {}
                )
            }
        )
    }
}
