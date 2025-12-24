package com.rahulpahuja.waves.module.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: ArtistProfileViewModel = hiltViewModel()
) {
    var stageName by remember { mutableStateOf("DJ Mandy") }
    var artistBio by remember { mutableStateOf("Electronic music enthusiast specializing in Deep House and Techno. Building atmospheres that move the soul and the feet.") }
    
    // Social Links
    var instagram by remember { mutableStateOf("instagram.com/djkicks_official") }
    var soundcloud by remember { mutableStateOf("soundcloud.com/djkicks") }
    var mixcloud by remember { mutableStateOf("") }
    var facebook by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color(0xFF10141D),
        topBar = {
            TopAppBar(
                title = { 
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Artist Profile", color = Color.White, fontWeight = FontWeight.Bold)
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
            Button(
                onClick = { /* Save Profile */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2962FF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.Save, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Profile", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Profile Image
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.Gray.copy(alpha = 0.3f))
                        ) {
                            // Image placeholder
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
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tap to update photo", color = Color.Gray, fontSize = 12.sp)
                }
            }

            // Stage Name
            Column {
                Text("STAGE NAME", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = stageName,
                    onValueChange = { stageName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E232F)),
                    leadingIcon = { Icon(Icons.Filled.GraphicEq, contentDescription = null, tint = Color.Gray) }, // Using graphic eq as placeholder for 'Un' icon
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1E232F),
                        unfocusedContainerColor = Color(0xFF1E232F),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White
                    )
                )
            }

            // Artist Bio
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("ARTIST BIO", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("${artistBio.length}/300", color = Color.Gray, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = artistBio,
                    onValueChange = { if (it.length <= 300) artistBio = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
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

            // Connect Your Sound
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("CONNECT YOUR SOUND", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                
                SocialLinkInput(
                    value = instagram,
                    placeholder = "Paste Instagram URL",
                    icon = Icons.Filled.CameraAlt,
                    isLinked = instagram.isNotEmpty()
                ) { instagram = it }

                SocialLinkInput(
                    value = soundcloud,
                    placeholder = "Paste SoundCloud URL",
                    icon = Icons.Filled.GraphicEq,
                    isLinked = soundcloud.isNotEmpty()
                ) { soundcloud = it }

                SocialLinkInput(
                    value = mixcloud,
                    placeholder = "Paste Mixcloud URL",
                    icon = Icons.Filled.Cloud,
                    isLinked = mixcloud.isNotEmpty()
                ) { mixcloud = it }
                
                SocialLinkInput(
                    value = facebook,
                    placeholder = "Paste Facebook Page URL",
                    icon = Icons.Filled.Public,
                    isLinked = facebook.isNotEmpty()
                ) { facebook = it }
            }

            // Academy Progress
            Column {
                Text("ACADEMY PROGRESS", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E232F)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Circular Progress Placeholder
                        Box(
                            modifier = Modifier.size(64.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                progress = { 0.75f },
                                modifier = Modifier.fillMaxSize(),
                                color = Color(0xFF2962FF),
                                trackColor = Color(0xFF10141D),
                            )
                            Text("75%", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Intermediate DJ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).background(Color(0xFF2962FF), CircleShape))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("32h Studio", color = Color.Gray, fontSize = 12.sp)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).background(Color(0xFF2962FF), CircleShape))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("4 Tracks", color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun SocialLinkInput(
    value: String,
    placeholder: String,
    icon: ImageVector,
    isLinked: Boolean,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.Gray) },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1E232F)),
        leadingIcon = { 
            Icon(
                icon, 
                contentDescription = null, 
                tint = if(isLinked) Color(0xFF2962FF) else Color.Gray 
            ) 
        },
        trailingIcon = {
            if (isLinked) {
                Icon(Icons.Filled.CheckCircle, contentDescription = "Linked", tint = Color(0xFF2962FF))
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1E232F),
            unfocusedContainerColor = Color(0xFF1E232F),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White
        ),
        singleLine = true
    )
}

@HiltViewModel
class ArtistProfileViewModel @Inject constructor() : ViewModel() {
    // Add logic here
}
