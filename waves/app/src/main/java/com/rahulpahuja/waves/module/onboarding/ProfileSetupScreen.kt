package com.rahulpahuja.waves.module.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rahulpahuja.waves.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(onContinueClick: () -> Unit, onSkipClick: () -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = Color(0xFF10141D),
        topBar = {
            TopAppBar(
                title = { Text("Profile Setup", color = Color.White, fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onSkipClick) {
                        Text("Skip", color = Color.Gray)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF10141D))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Let\'s get you set up",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Add your details so the team knows who\'s spinning the tracks.",
                color = Color.Gray,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            Box(contentAlignment = Alignment.BottomEnd) {
                Image(
                    painter = painterResource(id = R.drawable.profile_placeholder),
                    contentDescription = "Upload Photo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = 0.2f))
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2962FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.CameraAlt,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Text("Upload Photo (Optional)", color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                placeholder = { Text("e.g. DJ Shadow") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2962FF),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            ExposedDropdownMenuBox(expanded = false, onExpandedChange = {}) {
                OutlinedTextField(
                    value = selectedRole ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Role at School") },
                    placeholder = { Text("Select your role...") },
                    trailingIcon = { Icon(Icons.Filled.KeyboardArrowDown, null) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2962FF),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onContinueClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2962FF)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Continue",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
