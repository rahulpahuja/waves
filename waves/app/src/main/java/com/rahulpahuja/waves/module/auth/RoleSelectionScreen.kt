package com.rahulpahuja.waves.module.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import android.widget.Toast
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import com.rahulpahuja.waves.ui.navigation.Screen
import com.rahulpahuja.waves.ui.theme.WavesTheme

@Composable
fun RoleSelectionScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(authState) {
        android.util.Log.d("RoleSelectionScreen", "Current AuthState: $authState")
        if (authState is AuthState.PendingApproval) {
            android.util.Log.d("RoleSelectionScreen", "Navigating to WaitingApproval")
            navController.navigate(Screen.WaitingApproval.route) {
                popUpTo(Screen.RoleSelection.route) { inclusive = true }
            }
        }
    }

    RoleSelectionContent(
        onRoleSelect = { viewModel.selectRole(it) }
    )
}

@Composable
fun RoleSelectionContent(
    onRoleSelect: (String) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to Waves Academy",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Please select your role to continue.",
                color = Color.Gray,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(48.dp))

            RoleCard(
                title = "Student",
                subtitle = "I'm here to learn music production.",
                icon = Icons.Default.Headphones,
                onClick = { onRoleSelect("student") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            RoleCard(
                title = "DJ",
                subtitle = "I'm a DJ looking to sharpen my skills.",
                icon = Icons.Default.Headphones,
                onClick = { onRoleSelect("dj") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            RoleCard(
                title = "Admin",
                subtitle = "I manage classes and students.",
                icon = Icons.Default.Settings,
                onClick = { onRoleSelect("admin") }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoleSelectionPreview() {
    WavesTheme {
        RoleSelectionContent(onRoleSelect = {})
    }
}

@Composable
fun RoleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF2962FF).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color(0xFF2962FF), modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}
