package com.rahulpahuja.waves.module.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rahulpahuja.waves.R
import com.rahulpahuja.waves.data.remote.PaymentRecord
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScreen(
    payment: PaymentRecord,
    onNavigateBack: () -> Unit
) {
    val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(payment.timestamp))
    val verifyDateStr = payment.verificationDate?.let { 
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(it)) 
    } ?: "N/A"

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Receipt", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Print/PDF */ }) {
                        Icon(Icons.Filled.Print, contentDescription = "Print", tint = Color.White)
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo
                    Image(
                        painter = painterResource(id = R.drawable.logo_placeholder),
                        contentDescription = "Waves Academy Logo",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("WAVES ACADEMY", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Official Payment Receipt", color = Color.Gray, fontSize = 12.sp)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(24.dp))

                    ReceiptRow("Receipt ID", payment.receiptId ?: "N/A")
                    ReceiptRow("Date", dateStr)
                    ReceiptRow("Student", payment.userName)
                    ReceiptRow("Course", payment.courseName)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F9FF), RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("TOTAL AMOUNT", color = Color.Black, fontWeight = FontWeight.Bold)
                            Text("₹${payment.amount}", color = Color(0xFF2962FF), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Status: VERIFIED", color = Color(0xFF00E676), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Verified on $verifyDateStr", color = Color.Gray, fontSize = 11.sp)
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        "Thank you for choosing Waves Academy.\nKeep making great music!",
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { /* Download */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.Download, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Download Receipt (PDF)")
            }
        }
    }
}

@Composable
fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, color = Color.Black, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }
}
