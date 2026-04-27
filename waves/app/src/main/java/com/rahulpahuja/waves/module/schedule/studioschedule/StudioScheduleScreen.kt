package com.rahulpahuja.waves.module.schedule.studioschedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahulpahuja.waves.data.remote.BookingRequest
import com.rahulpahuja.waves.ui.theme.AppTheme
import com.rahulpahuja.waves.ui.theme.WavesTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StudioScheduleScreen(
    onNavigateBack: () -> Unit,
    viewModel: StudioScheduleViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    StudioScheduleContent(
        state = state,
        onNavigateBack = onNavigateBack,
        onBookSlot = { viewModel.requestBooking(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioScheduleContent(
    state: StudioScheduleUiState,
    onNavigateBack: () -> Unit,
    onBookSlot: (Slot) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
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
                     val days = listOf("S", "M", "T", "W", "T", "F", "S")
                     for (i in 0..6) {
                         Column(horizontalAlignment = Alignment.CenterHorizontally) {
                             Text(days[i], color = Color.Gray, fontSize = 12.sp)
                             Spacer(modifier = Modifier.height(8.dp))
                             if (i == 3) {
                                 Box(
                                     modifier = Modifier
                                         .size(32.dp)
                                         .background(Color(0xFF2962FF), CircleShape),
                                     contentAlignment = Alignment.Center
                                 ) {
                                     Text("${state.selectedDate}", color = Color.White, fontWeight = FontWeight.Bold)
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
                Text("Available Slots (9 AM - 5 PM)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                
                state.availableSlots.forEach { slot ->
                    SlotItem(slot, onBook = { onBookSlot(slot) })
                }
            }

            // My Bookings
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("My Requests & History", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    if (state.myBookings.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF2962FF), CircleShape)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("${state.myBookings.size}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StudioScheduleScreenPreview() {
    WavesTheme(colorScheme = AppTheme.ADMIN_SLATE.colorScheme()) {
        StudioScheduleContent(
            state = StudioScheduleUiState(
                selectedDate = 5,
                availableSlots = listOf(
                    Slot("1", "Studio 1", "Main Booth", "09:00", "AM", true),
                    Slot("2", "Studio 2", "Secondary Booth", "11:00", "AM", false)
                ),
                myBookings = listOf(
                    BookingRequest(id = "1", userId = "1", startTime = System.currentTimeMillis(), status = "APPROVED", type = "CHECKIN"),
                    BookingRequest(id = "2", userId = "1", startTime = System.currentTimeMillis() + 86400000, status = "PENDING", type = "BOOKING")
                )
            ),
            onNavigateBack = {},
            onBookSlot = {}
        )
    }
}

@Composable
fun SlotItem(slot: Slot, onBook: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { if(slot.isAvailable) onBook() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                    fontSize = 14.sp
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
fun BookingItem(booking: BookingRequest) {
    val accentColor = when (booking.status) {
        "APPROVED" -> Color(0xFF00E676)
        "PENDING" -> Color(0xFFFFC107)
        "REJECTED" -> Color(0xFFE91E63)
        else -> Color.Gray
    }
    
    val dateTimeStr = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault()).format(Date(booking.startTime))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .background(accentColor.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(booking.status, color = accentColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(dateTimeStr, color = Color.Gray, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(if(booking.type == "CHECKIN") "Live Check-in" else "Scheduled Session", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            
            if (booking.status == "REJECTED" && !booking.rejectionReason.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = Color.Red.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Reason: ${booking.rejectionReason}",
                        color = Color(0xFFFF8A80),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
