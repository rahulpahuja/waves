package com.rahulpahuja.waves.module.admin.usermanagement

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahulpahuja.waves.data.remote.FirestoreUser
import com.rahulpahuja.waves.data.remote.NotificationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreen(
    onNavigateBack: () -> Unit,
    onAddUserClick: () -> Unit,
    viewModel: UserManagementViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Student", "Instructor", "Admin")

    var selectedUserForNotification by remember { mutableStateOf<FirestoreUser?>(null) }

    val filteredUsers = users.filter { user ->
        (selectedFilter == "All" || user.role.equals(selectedFilter, ignoreCase = true)) &&
        (user.displayName.contains(searchQuery, ignoreCase = true) || user.email.contains(searchQuery, ignoreCase = true))
    }

    if (selectedUserForNotification != null) {
        NotificationDialog(
            user = selectedUserForNotification!!,
            onDismiss = { selectedUserForNotification = null },
            onSend = { message, type ->
                viewModel.notifyUser(selectedUserForNotification!!.uid, message, type)
                selectedUserForNotification = null
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("User Management", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = onAddUserClick) {
                        Icon(Icons.Default.Add, contentDescription = "Add User", tint = Color.White)
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
                .padding(horizontal = 16.dp)
        ) {
            // Search Bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface),
                placeholder = { Text("Search users...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1E232F),
                    unfocusedContainerColor = Color(0xFF1E232F),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filters
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                filters.forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF2962FF),
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = Color.Gray
                        ),
                        border = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF2962FF))
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filteredUsers) { user ->
                        UserItem(
                            user = user,
                            onPromote = { viewModel.promoteToInstructor(user) },
                            onNotify = { selectedUserForNotification = user }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(user: FirestoreUser, onPromote: () -> Unit, onNotify: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(user.displayName.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(user.displayName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(user.role.replaceFirstChar { it.uppercase() }, color = Color.Gray, fontSize = 12.sp)
                Text(user.email, color = Color.Gray.copy(alpha = 0.7f), fontSize = 10.sp)
            }
            
            IconButton(onClick = onNotify) {
                Icon(Icons.Default.Notifications, contentDescription = "Notify User", tint = Color.Gray)
            }

            if (user.role != "instructor" && user.role != "admin") {
                IconButton(onClick = onPromote) {
                    Icon(Icons.Default.Stars, contentDescription = "Promote to Instructor", tint = Color(0xFFFFD700))
                }
            }
        }
    }
}

@Composable
fun NotificationDialog(
    user: FirestoreUser,
    onDismiss: () -> Unit,
    onSend: (String, NotificationType) -> Unit
) {
    var message by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(NotificationType.GENERAL) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Notify ${user.displayName}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Select Notification Type:", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NotificationTypeChip(
                        label = "Absent",
                        selected = selectedType == NotificationType.ABSENTEEISM,
                        onClick = { 
                            selectedType = NotificationType.ABSENTEEISM
                            message = "You were marked absent for your recent class. Please contact the academy if this is an error."
                        }
                    )
                    NotificationTypeChip(
                        label = "Payment",
                        selected = selectedType == NotificationType.PAYMENT_DUE,
                        onClick = { 
                            selectedType = NotificationType.PAYMENT_DUE
                            message = "This is a reminder that your fee payment is due. Please clear it at the earliest."
                        }
                    )
                    NotificationTypeChip(
                        label = "Other",
                        selected = selectedType == NotificationType.GENERAL,
                        onClick = { 
                            selectedType = NotificationType.GENERAL
                            message = ""
                        }
                    )
                }
                
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Message") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSend(message, selectedType) },
                enabled = message.isNotBlank()
            ) {
                Text("Send")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun NotificationTypeChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, fontSize = 12.sp) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFF2962FF),
            selectedLabelColor = Color.White
        )
    )
}
