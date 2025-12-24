package com.rahulpahuja.waves.module.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
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
fun StudioScheduleScreen(
    onNavigateBack: () -> Unit,
    viewModel: StudioScheduleViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color(0xFF10141D),
        topBar = {
            TopAppBar(
                title = { 
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Studio Schedule", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Filter */ }) {
                        Icon(Icons.Filled.FilterList, contentDescription = "Filter", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF10141D))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Add Booking */ },
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
            // Calendar Strip
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Prev Month */ }) {
                        Icon(Icons.Filled.ChevronLeft, contentDescription = null, tint = Color.Gray)
                    }
                    Text("September 2023", color = Color.White, fontWeight = FontWeight.Bold)
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
                     // Generating a few dates
                     val days = listOf("S", "M", "T", "W", "T", "F", "S")
                     // Just static UI for demo
                     for (i in 0..6) {
                         Column(horizontalAlignment = Alignment.CenterHorizontally) {
                             Text(days[i], color = Color.Gray, fontSize = 12.sp)
                             Spacer(modifier = Modifier.height(8.dp))
                             if (i == 3) { // Selected day (Wednesday)
                                 Box(
                                     modifier = Modifier
                                         .size(32.dp)
                                         .background(Color(0xFF2962FF), CircleShape),
                                     contentAlignment = Alignment.Center
                                 ) {
                                     Text("5", color = Color.White, fontWeight = FontWeight.Bold)
                                 }
                             } else {
                                 Text("${2 + i}", color = Color.White)
                             }
                         }
                     }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(Color(0xFF1E232F), RoundedCornerShape(2.dp)))
            }

            // Available Slots
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Available Slots", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                
                state.availableSlots.forEach { slot ->
                    SlotItem(slot)
                }
            }

            // My Bookings
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("My Bookings", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF2962FF), CircleShape)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("3", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                state.myBookings.forEach { booking ->
                    BookingItem(booking)
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun SlotItem(slot: Slot) {
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(slot.time, color = if(slot.isAvailable) Color(0xFF2962FF) else Color.Gray, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(slot.period, color = if(slot.isAvailable) Color(0xFF2962FF) else Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .background(Color.Gray.copy(alpha = 0.3f))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = slot.title,
                    color = if(slot.isAvailable) Color.White else Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    style = if(!slot.isAvailable) androidx.compose.ui.text.TextStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough) else androidx.compose.ui.text.TextStyle()
                )
                Text(slot.subtitle, color = Color.Gray, fontSize = 12.sp)
            }
            if (slot.isAvailable) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFF2962FF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Book", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun BookingItem(booking: Booking) {
    val statusColor = when (booking.status) {
        BookingStatus.CONFIRMED -> Color(0xFF00E676) // Green (Confirming UI) - Though image uses Blue for Confirmed tag background
        BookingStatus.PENDING -> Color(0xFFFFC107) // Yellow
        BookingStatus.MISSED -> Color(0xFFE91E63) // Red
    }
    
    val statusText = when (booking.status) {
        BookingStatus.CONFIRMED -> "CONFIRMED"
        BookingStatus.PENDING -> "PENDING"
        BookingStatus.MISSED -> "MISSED"
    }
    
    val accentColor = when (booking.status) {
        BookingStatus.CONFIRMED -> Color(0xFF2962FF)
        BookingStatus.PENDING -> Color(0xFFFFC107)
        BookingStatus.MISSED -> Color(0xFFE91E63)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E232F)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            // Status Strip
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(accentColor)
            )
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(accentColor.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(statusText, color = accentColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(booking.dateTime, color = Color.Gray, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(booking.title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(booking.location, color = Color.Gray, fontSize = 12.sp)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { /* Action based on status */ },
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10141D)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    val buttonText = when(booking.status) {
                        BookingStatus.CONFIRMED -> "Details"
                        BookingStatus.PENDING -> "Edit"
                        BookingStatus.MISSED -> "Reschedule"
                    }
                    Text(buttonText, color = Color.White, fontSize = 12.sp)
                }
            }
            
            if (booking.status == BookingStatus.PENDING) {
                 IconButton(
                     onClick = { /* Cancel */ },
                     modifier = Modifier.align(Alignment.CenterVertically).padding(end = 8.dp)
                 ) {
                     Icon(Icons.Filled.Close, contentDescription = "Cancel", tint = Color.Red, modifier = Modifier.size(16.dp))
                 }
            }
        }
    }
}
