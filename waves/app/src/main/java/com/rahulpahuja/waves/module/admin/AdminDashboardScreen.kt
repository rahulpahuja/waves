package com.rahulpahuja.waves.module.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahulpahuja.waves.R

@Composable
fun AdminDashboardScreen(
    viewModel: AdminDashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val backgroundColor = Color(0xFF10141D)

    Scaffold(
        bottomBar = { AdminBottomBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                containerColor = Color(0xFF2962FF),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_content_description))
            }
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { HeaderSection() }
            item { OverviewSection(state) }
            item { ManageSchoolSection() }
            item { UpcomingSessionsSection(state.sessions) }
            item { Spacer(modifier = Modifier.height(80.dp)) } // Bottom padding for FAB/Nav
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0C9A6)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.profile_content_description),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = stringResource(R.string.welcome_back),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Text(
                    text = stringResource(R.string.default_user_name),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = stringResource(R.string.notifications_content_description),
                tint = Color.White
            )
        }
    }
}

@Composable
fun OverviewSection(state: AdminDashboardUiState) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.overview_title),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = stringResource(R.string.view_report),
                color = Color(0xFF2962FF),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OverviewCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.students_title),
                value = "${state.studentCount}",
                icon = Icons.Default.Person,
                growth = "${state.studentGrowth}%",
                iconColor = Color(0xFF2962FF)
            )
            OverviewCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.revenue_title),
                value = "$${state.revenue}k",
                icon = Icons.Default.AttachMoney,
                growth = "${state.revenueGrowth}%",
                iconColor = Color(0xFF00E676)
            )
        }
    }
}

@Composable
fun OverviewCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    growth: String,
    iconColor: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E232F)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(iconColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconColor)
                }
                Box(
                    modifier = Modifier
                        .background(Color(0xFF00E676).copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "↗ $growth",
                        color = Color(0xFF00E676),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                Text(
                    text = title,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = value,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ManageSchoolSection() {
    Column {
        Text(
            text = stringResource(R.string.manage_school_title),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ManageCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.enrollment_title),
                    subtitle = stringResource(R.string.manage_students_subtitle),
                    icon = Icons.Default.Person,
                    colorStart = Color(0xFF4A3423), // Dark Brownish
                    colorEnd = Color(0xFF2C1E14)
                )
                ManageCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.attendance_title),
                    subtitle = stringResource(R.string.track_checkins_subtitle),
                    icon = Icons.Default.DateRange,
                    colorStart = Color(0xFF3E4A59), // Blueish Grey
                    colorEnd = Color(0xFF242A33)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ManageCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.finances_title),
                    subtitle = stringResource(R.string.logs_payments_subtitle),
                    icon = Icons.Default.AttachMoney,
                    colorStart = Color(0xFF2E3B3B), // Greenish Grey
                    colorEnd = Color(0xFF1B2424)
                )
                ManageCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.media_title),
                    subtitle = stringResource(R.string.school_gallery_subtitle),
                    icon = Icons.Default.Image,
                    colorStart = Color(0xFF2B3A42), // Dark Slate
                    colorEnd = Color(0xFF192226)
                )
            }
        }
    }
}

@Composable
fun ManageCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    colorStart: Color,
    colorEnd: Color
) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(colorStart, colorEnd)
                    )
                )
        ) {
            // Background Image Placeholder (Optional - using gradients for now)
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = subtitle,
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun UpcomingSessionsSection(sessions: List<Session>) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.upcoming_sessions_title),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = stringResource(R.string.see_all),
                color = Color(0xFF2962FF),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            sessions.forEach { session ->
                SessionItem(session)
            }
        }
    }
}

@Composable
fun SessionItem(session: Session) {
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.today_label),
                    color = Color.Gray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = session.time,
                    color = Color(0xFF2962FF),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .background(Color.DarkGray)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = session.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = session.details,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
            Icon(
                imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(R.string.details_content_description),
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun AdminBottomBar() {
    NavigationBar(
        containerColor = Color(0xFF10141D),
        contentColor = Color.White
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { /*TODO*/ },
            icon = { Icon(Icons.Default.Home, contentDescription = stringResource(R.string.home_nav)) },
            label = { Text(stringResource(R.string.home_nav)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF2962FF),
                selectedTextColor = Color(0xFF2962FF),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon(Icons.Default.CalendarToday, contentDescription = stringResource(R.string.calendar_nav)) },
            label = { Text(stringResource(R.string.calendar_nav)) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon(Icons.Default.School, contentDescription = stringResource(R.string.students_nav)) },
            label = { Text(stringResource(R.string.students_nav)) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings_title)) },
            label = { Text(stringResource(R.string.settings_title)) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
    }
}
