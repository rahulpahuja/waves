package com.rahulpahuja.waves.module.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun WaitingApprovalScreen(
    onLogout: () -> Unit,
    onApproved: (Boolean) -> Unit,
    viewModel: WaitingApprovalViewModel = hiltViewModel()
) {
    val status by viewModel.userStatus.collectAsState()
    val role by viewModel.userRole.collectAsState()

    LaunchedEffect(status) {
        if (status == "APPROVED") {
            onApproved(role == "admin")
        }
    }

    Scaffold(
        containerColor = Color(0xFF10141D)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.HourglassEmpty,
                contentDescription = null,
                tint = Color(0xFF2962FF),
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Registration Submitted",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Your account is currently being reviewed by a Super Admin. You will be able to access the app once your registration is approved.",
                color = Color.Gray,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E232F)),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Log Out", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
