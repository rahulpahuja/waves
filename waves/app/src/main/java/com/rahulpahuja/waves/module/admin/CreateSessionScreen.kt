package com.rahulpahuja.waves.module.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSessionScreen(
    onNavigateBack: () -> Unit,
    onPublish: () -> Unit,
    viewModel: CreateSessionViewModel = hiltViewModel()
) {
    var sessionName by remember { mutableStateOf("") }
    var courseFee by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("DJing") }
    var startTime by remember { mutableStateOf("06:00 PM") }
    var endTime by remember { mutableStateOf("08:00 PM") }
    var capacity by remember { mutableIntStateOf(12) }
    var selectedStudio by remember { mutableStateOf("Main Room") }
    var topics by remember { mutableStateOf(listOf<String>()) }
    var newTopic by remember { mutableStateOf("") }

    val categories = listOf("DJing", "Production", "Theory")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Create Session", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            val isLoading by viewModel.isLoading.collectAsState()
            Button(
                onClick = {
                    viewModel.publishCourse(
                        name = sessionName,
                        description = "Course in $selectedCategory",
                        fee = courseFee.toDoubleOrNull() ?: 0.0,
                        duration = 8,
                        topics = topics,
                        category = selectedCategory,
                        onComplete = onPublish
                    )
                },
                enabled = !isLoading && sessionName.isNotEmpty() && courseFee.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Publish Session", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
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
            // Session Name
            Column {
                Text("SESSION NAME", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = sessionName,
                    onValueChange = { sessionName = it },
                    placeholder = { Text("e.g., Techno Mixing 101", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1E232F),
                        unfocusedContainerColor = Color(0xFF1E232F),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }

            // Course Fee
            Column {
                Text("COURSE FEE (₹)", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = courseFee,
                    onValueChange = { if (it.all { char -> char.isDigit() }) courseFee = it },
                    placeholder = { Text("e.g., 15000", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1E232F),
                        unfocusedContainerColor = Color(0xFF1E232F),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }

            // Category
            Column {
                Text("CATEGORY", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category) },
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
            }

            // Date & Time (Calendar placeholder)
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("DATE & TIME", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Today", color = Color(0xFF2962FF), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Placeholder for Calendar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Calendar Placeholder", color = Color.Gray)
                }
            }

            // Start & End Time
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("START", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(startTime, color = Color.White, fontSize = 16.sp)
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("END", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(endTime, color = Color.White, fontSize = 16.sp)
                    }
                }
            }

            // Instructor
            Column {
                Text("INSTRUCTOR", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFE0C9A6), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Marcus Chen", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Techno Specialist", color = Color.Gray, fontSize = 12.sp)
                    }
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color.Gray)
                }
            }

            // Capacity & Studio
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Capacity
                Column(modifier = Modifier.weight(1f)) {
                    Text("CAPACITY", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { if (capacity > 0) capacity-- },
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFF10141D), RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Filled.Remove, contentDescription = "Decrease", tint = Color.Gray)
                        }
                        Text("$capacity", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        IconButton(
                            onClick = { capacity++ },
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFF2962FF), RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Increase", tint = Color.White)
                        }
                    }
                }

                // Studio
                Column(modifier = Modifier.weight(1f)) {
                    Text("STUDIO", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(selectedStudio, color = Color.White, fontSize = 16.sp)
                    }
                }
            }
            // Topics
            Column {
                Text("TOPICS", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = newTopic,
                        onValueChange = { newTopic = it },
                        placeholder = { Text("e.g., Beatmatching", color = Color.Gray) },
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1E232F),
                            unfocusedContainerColor = Color(0xFF1E232F),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )
                    IconButton(
                        onClick = {
                            if (newTopic.isNotBlank()) {
                                topics = topics + newTopic.trim()
                                newTopic = ""
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF2962FF), RoundedCornerShape(12.dp))
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add topic", tint = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                topics.forEachIndexed { index, topic ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${index + 1}. $topic",
                            color = Color.White,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { topics = topics.filterIndexed { i, _ -> i != index } },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Filled.Close, contentDescription = "Remove", tint = Color.Gray, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
