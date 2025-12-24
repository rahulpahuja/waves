package com.rahulpahuja.waves.module.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onNavigateBack: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val messageInput by viewModel.messageInput.collectAsState()
    val listState = rememberLazyListState()

    // Scroll to bottom when messages change
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Scaffold(
        containerColor = Color(0xFF10141D),
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.Gray.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                             Icon(Icons.Filled.Person, contentDescription = null, tint = Color.Gray)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(state.contactName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(6.dp).background(Color(0xFF00E676), CircleShape))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(state.contactRole, color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Calendar */ }) {
                        Icon(Icons.Filled.CalendarToday, contentDescription = "Schedule", tint = Color(0xFF2962FF))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF10141D))
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF10141D))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = { /* Add Attachment */ },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF1E232F), CircleShape)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
                    }
                    
                    TextField(
                        value = messageInput,
                        onValueChange = { viewModel.onMessageInputChange(it) },
                        placeholder = { Text("Discuss feedback...", color = Color.Gray) },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color(0xFF1E232F)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1E232F),
                            unfocusedContainerColor = Color(0xFF1E232F),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White
                        ),
                        trailingIcon = {
                            Icon(Icons.Filled.Mic, contentDescription = "Voice", tint = Color.Gray)
                        }
                    )
                    
                    IconButton(
                        onClick = { viewModel.sendMessage() },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF2962FF), CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.messages) { message ->
                MessageItem(message)
            }
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    val alignment = if (message.isMe) Alignment.End else Alignment.Start
    val bubbleColor = if (message.isMe) Color(0xFF2962FF) else Color(0xFF1E232F)
    val shape = if (message.isMe) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp)
    } else {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        if (!message.isMe) {
             Row(verticalAlignment = Alignment.Bottom) {
                 Box(
                     modifier = Modifier
                         .size(32.dp)
                         .clip(CircleShape)
                         .background(Color.Gray.copy(alpha = 0.3f)),
                     contentAlignment = Alignment.Center
                 ) {
                     Icon(Icons.Filled.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                 }
                 Spacer(modifier = Modifier.width(8.dp))
                 MessageContent(message, bubbleColor, shape)
             }
             Spacer(modifier = Modifier.height(4.dp))
             Text(message.timestamp, color = Color.Gray, fontSize = 10.sp, modifier = Modifier.padding(start = 40.dp))
        } else {
             MessageContent(message, bubbleColor, shape)
             Spacer(modifier = Modifier.height(4.dp))
             Row(modifier = Modifier.align(Alignment.End), verticalAlignment = Alignment.CenterVertically) {
                 Text(message.timestamp, color = Color.Gray, fontSize = 10.sp)
                 // Check marks for read receipt (static for now)
                 // Icon(Icons.Default.DoneAll, contentDescription = null, tint = Color(0xFF2962FF), modifier = Modifier.size(12.dp))
             }
        }
    }
}

@Composable
fun MessageContent(message: Message, color: Color, shape: androidx.compose.ui.graphics.Shape) {
    Box(
        modifier = Modifier
            .background(color, shape)
            .padding(12.dp)
    ) {
        when (message.type) {
            MessageType.TEXT -> {
                Text(message.content, color = Color.White, fontSize = 14.sp)
            }
            MessageType.AUDIO -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFF2962FF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = "Play", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(message.content, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                         // Audio wave placeholder
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            repeat(10) {
                                Box(modifier = Modifier.width(2.dp).height((10..20).random().dp).background(Color(0xFF2962FF)))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(message.duration ?: "", color = Color.Gray, fontSize = 12.sp)
                }
            }
            MessageType.IMAGE -> {
                Column {
                     // Placeholder Image
                     Box(
                         modifier = Modifier
                             .fillMaxWidth()
                             .height(150.dp)
                             .background(Color.Gray)
                     ) {
                         // Overlaying waveform for the "aggressive curve" context in the screenshot
                         Icon(Icons.Filled.GraphicEq, contentDescription = null, tint = Color.White, modifier = Modifier.fillMaxSize())
                     }
                     if (message.content.isNotEmpty()) {
                         Spacer(modifier = Modifier.height(8.dp))
                         Text(message.content, color = Color.White, fontSize = 14.sp)
                     }
                }
            }
        }
    }
}
