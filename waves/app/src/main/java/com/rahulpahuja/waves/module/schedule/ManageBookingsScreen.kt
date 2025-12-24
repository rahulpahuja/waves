package com.rahulpahuja.waves.module.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageBookingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: ManageBookingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color(0xFF10141D),
        topBar = {
            TopAppBar(
                title = { 
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text("Master Schedule", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Manage bookings & attendance", color = Color.Gray, fontSize = 12.sp)
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { /* Notifications */ }) {
                            Icon(Icons.Filled.Notifications, contentDescription = "Notifications", tint = Color.Gray)
                        }
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color.Red, CircleShape)
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF10141D))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Add */ },
                containerColor = Color(0xFF2962FF),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
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
            // Room Filters
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { /* All Studios */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2962FF)),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("All Studios", fontSize = 12.sp)
                }
                Button(
                    onClick = { /* DJ Room */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("DJ Room", fontSize = 12.sp)
                }
                Button(
                    onClick = { /* Production Lab */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Production Lab", fontSize = 12.sp)
                }
            }

            // Calendar Strip (Simplified)
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Prev Month */ }) {
                        Icon(Icons.Filled.ChevronLeft, contentDescription = null, tint = Color.Gray)
                    }
                    Text("October 2023", color = Color.White, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { /* Next Month */ }) {
                        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Simple Date Row Placeholder
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val days = listOf("S", "M", "T", "W", "T", "F", "S")
                    val dates = listOf(1, 2, 3, 4, 5, 6, 7) // Fake dates
                    for (i in 0..6) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(days[i], color = Color.Gray, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            if (i == 4) { // Selected day (Thursday 5th)
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(Color(0xFF2962FF), RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("${dates[i]}", color = Color.White, fontWeight = FontWeight.Bold)
                                        Box(modifier = Modifier.size(4.dp).background(Color.White, CircleShape))
                                    }
                                }
                            } else {
                                Text("${dates[i]}", color = Color.White)
                                if (i == 5 || i == 6) { // Dots for events
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(modifier = Modifier.size(4.dp).background(if (i==5) Color(0xFFFFC107) else Color(0xFF00E676), CircleShape))
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LegendItem("Full", Color(0xFF00E676))
                    Spacer(modifier = Modifier.width(16.dp))
                    LegendItem("Pending", Color(0xFFFFC107))
                    Spacer(modifier = Modifier.width(16.dp))
                    LegendItem("Open", Color.Gray) // Using gray for empty circle representation
                }
            }

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF1E232F)))

            // Header for selected date
            Column {
                Text(state.date, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(state.stats, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.PendingActions, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("PENDING REQUESTS", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Pending Requests
            state.pendingRequests.forEach { request ->
                PendingRequestCard(request, 
                    onApprove = { viewModel.approveRequest(request.id) },
                    onReject = { viewModel.rejectRequest(request.id) }
                )
            }

            // Confirmed Sessions Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Check, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("CONFIRMED SESSIONS", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            // Confirmed Sessions
            state.confirmedSessions.forEach { session ->
                ConfirmedSessionCard(session)
            }
            
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun LegendItem(text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
fun PendingRequestCard(
    request: BookingRequest,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White), // Assuming white card based on image
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.LightGray, CircleShape), // Placeholder
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(request.name, color = Color.Black, fontWeight = FontWeight.Bold)
                        Text(request.details, color = Color.Gray, fontSize = 12.sp)
                    }
                }
                Box(
                    modifier = Modifier
                        .background(Color(0xFFFFC107).copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("Pending", color = Color(0xFFFFC107), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Schedule, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(request.time, color = Color.Black, fontSize = 12.sp)
                }
                if (request.location.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Location icon placeholder or similar
                        Text(request.location, color = Color.Black, fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onReject,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F5F5), contentColor = Color.Black),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Filled.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reject", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onApprove,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2962FF), contentColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Approve", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ConfirmedSessionCard(session: ConfirmedSession) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F9FF)), // Very light blue/gray
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                 Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.LightGray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                     // Initials
                    Text(session.name.take(1), fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(session.name, color = Color.Black, fontWeight = FontWeight.Bold)
                    Text(session.location, color = Color.Gray, fontSize = 12.sp)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(session.time, color = Color.Black, fontWeight = FontWeight.Bold)
                Text(session.duration, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}
