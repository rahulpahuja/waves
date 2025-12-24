package com.rahulpahuja.waves.module.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahulpahuja.waves.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val darkMode by viewModel.darkMode.collectAsState()
    val language by viewModel.language.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    SettingsScreenContent(
        darkMode = darkMode,
        language = language,
        notifications = notifications,
        onNavigateBack = onNavigateBack,
        onLogout = onLogout,
        onDarkModeChange = { viewModel.onDarkModeChange(it) },
        onNotificationsChange = { viewModel.onNotificationsChange(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    darkMode: Boolean,
    language: String,
    notifications: Boolean,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    onDarkModeChange: (Boolean) -> Unit,
    onNotificationsChange: (Boolean) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFF10141D),
        topBar = {
            TopAppBar(
                title = { 
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.settings_title), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back_content_description), tint = Color.White)
                    }
                },
                actions = { Spacer(modifier = Modifier.width(48.dp)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF10141D))
            )
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
            Spacer(modifier = Modifier.height(8.dp))
            
            // Preferences
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(stringResource(R.string.preferences_section_title), color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                
                SettingsToggleItem(
                    title = stringResource(R.string.dark_mode_title),
                    icon = Icons.Filled.DarkMode,
                    iconColor = Color(0xFF2962FF),
                    checked = darkMode,
                    onCheckedChange = onDarkModeChange
                )
                
                SettingsNavigationItem(
                    title = stringResource(R.string.language_title),
                    value = language,
                    icon = Icons.Filled.Language,
                    iconColor = Color(0xFF00E676),
                    onClick = { /* TODO */ }
                )
                
                SettingsToggleItem(
                    title = stringResource(R.string.notifications_title),
                    icon = Icons.Filled.Notifications,
                    iconColor = Color(0xFFE91E63),
                    checked = notifications,
                    onCheckedChange = onNotificationsChange
                )
            }
            
            // Support
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(stringResource(R.string.support_section_title), color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                
                SettingsNavigationItem(
                    title = stringResource(R.string.help_center_title),
                    icon = Icons.Filled.Help,
                    iconColor = Color(0xFF2962FF),
                    onClick = { /* TODO */ }
                )
                
                SettingsNavigationItem(
                    title = stringResource(R.string.report_bug_title),
                    icon = Icons.Filled.ReportProblem,
                    iconColor = Color(0xFFFFC107),
                    onClick = { /* TODO */ }
                )
                
                SettingsNavigationItem(
                    title = stringResource(R.string.contact_us_title),
                    icon = Icons.Filled.Email,
                    iconColor = Color(0xFF9C27B0),
                    onClick = { /* TODO */ }
                )
            }
            
            // Account
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(stringResource(R.string.account_section_title), color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                
                Card(
                    modifier = Modifier.fillMaxWidth().clickable(onClick = onLogout),
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
                                .background(Color(0xFF607D8B).copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Logout, contentDescription = null, tint = Color(0xFF607D8B), modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(stringResource(R.string.logout_title), color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
                
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { /* TODO: Delete Account */ },
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
                                .background(Color.Red.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Delete, contentDescription = null, tint = Color.Red, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(stringResource(R.string.delete_account_title), color = Color.Red, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.version_info),
                color = Color.Gray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreenContent(
        darkMode = false,
        language = "English",
        notifications = true,
        onNavigateBack = {},
        onLogout = {},
        onDarkModeChange = {},
        onNotificationsChange = {}
    )
}
