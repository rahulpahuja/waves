package com.rahulpahuja.waves.module.student

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rahulpahuja.waves.ui.navigation.Screen

@Composable
fun StudentDashboardScreen(
    navController: NavController,
    viewModel: StudentDashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val backgroundColor = Color(0xFF10141D)

    // This now delegates to the main student navigation
    StudentNavigation(navController = navController)
}

@Composable
fun DashboardContent(
    navController: NavController,
    viewModel: StudentDashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("GOOD EVENING", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Hey, ${state.userName}", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.clickable(onClick = { navController.navigate(Screen.Notifications.route) })) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E232F)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notifications", tint = Color.White)
                    }
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(0xFF2962FF), CircleShape)
                            .align(Alignment.TopEnd)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier.clickable(onClick = { navController.navigate(Screen.StudentSettings.route) })) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0C9A6)), // Placeholder color
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = Color.White)
                    }
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color(0xFF00E676), CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                }
            }
        }

        // Current Course Progress
        state.currentCourse?.let { course ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E232F)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF2962FF).copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Current Course", color = Color(0xFF2962FF), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Icon(Icons.Filled.BarChart, contentDescription = null, tint = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(course.title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${(course.progressPercentage * 100).toInt()}%", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        Text("${course.completedClasses}/${course.totalClasses} Classes Completed", color = Color.Gray, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { course.progressPercentage },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF2962FF),
                        trackColor = Color(0xFF2C3240)
                    )
                }
            }
        }

        // Next Session
        state.nextSession?.let { session ->
            Column {
                Text("NEXT SESSION", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E232F)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF10141D)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.size(60.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(session.month, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text(session.day, color = Color(0xFF2962FF), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(session.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Schedule, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(session.time, color = Color.Gray, fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(session.location, color = Color.Gray, fontSize = 12.sp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = { /* Check In */ },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2962FF)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Check In", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = { /* Reschedule */ },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Reschedule", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Quick Actions
        Column {
            Text("QUICK ACTIONS", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Payments",
                    icon = Icons.Filled.CreditCard,
                    iconColor = Color(0xFF9C27B0),
                    onClick = { navController.navigate(Screen.PaymentHistory.route) }
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Book Lab",
                    icon = Icons.Filled.Event,
                    iconColor = Color(0xFF2962FF),
                    onClick = { navController.navigate(Screen.StudioSchedule.route) }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Artist Profile",
                    icon = Icons.Filled.Person,
                    iconColor = Color(0xFFE91E63),
                    onClick = { navController.navigate(Screen.ArtistProfile.route) }
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Resources",
                    icon = Icons.Filled.Folder,
                    iconColor = Color(0xFFE65100),
                    onClick = { navController.navigate(Screen.MediaGallery.route) }
                )
            }
        }

        // Notification
        state.notification?.let { notification ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable(onClick = { navController.navigate(Screen.Notifications.route) }),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E232F)),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(8.dp).background(Color.Red, CircleShape))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "NEW: ",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = notification,
                        color = Color.Gray,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun QuickActionCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E232F)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(iconColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
fun StudentBottomBar(
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val items = listOf(
        Screen.StudentDashboard, 
        Screen.StudioSchedule, 
        Screen.MediaGallery, 
        Screen.StudentSettings
    )

    NavigationBar(
        containerColor = Color(0xFF10141D),
        contentColor = Color.White
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = { 
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = { 
                    val icon = when(screen) {
                        Screen.StudentDashboard -> Icons.Filled.Home
                        Screen.StudioSchedule -> Icons.Filled.CalendarToday
                        Screen.MediaGallery -> Icons.Filled.LibraryMusic
                        Screen.StudentSettings -> Icons.Filled.Person
                        else -> Icons.Filled.Home
                    }
                    Icon(icon, contentDescription = screen.route) 
                },
                label = { 
                    val label = when(screen) {
                        Screen.StudentDashboard -> "Home"
                        Screen.StudioSchedule -> "Schedule"
                        Screen.MediaGallery -> "Library"
                        Screen.StudentSettings -> "Profile"
                        else -> ""
                    }
                    Text(label) 
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF2962FF),
                    selectedTextColor = Color(0xFF2962FF),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
