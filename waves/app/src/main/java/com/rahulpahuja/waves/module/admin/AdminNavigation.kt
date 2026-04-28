package com.rahulpahuja.waves.module.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rahulpahuja.waves.module.admin.admindashboard.AdminBottomBar
import com.rahulpahuja.waves.module.admin.admindashboard.AdminDashboardScreen
import com.rahulpahuja.waves.module.admin.adminsettings.AdminSettingsScreen
import com.rahulpahuja.waves.module.admin.attendance.AttendanceScreen
import com.rahulpahuja.waves.module.admin.managecourses.ManageCoursesScreen
import com.rahulpahuja.waves.module.admin.students.StudentsScreen
import com.rahulpahuja.waves.module.admin.usermanagement.UserManagementScreen
import com.rahulpahuja.waves.module.auth.login.LoginViewModel
import com.rahulpahuja.waves.module.schedule.studioschedule.StudioScheduleScreen
import com.rahulpahuja.waves.ui.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun AdminNavigation(navController: NavController) {
    val adminNavController = rememberNavController()
    val backgroundColor = MaterialTheme.colorScheme.background
    val loginViewModel: LoginViewModel = hiltViewModel()
    
    // Smooth loading transition logic
    val navBackStackEntry by adminNavController.currentBackStackEntryAsState()
    var showTransitionLoading by remember { mutableStateOf(false) }

    LaunchedEffect(navBackStackEntry) {
        showTransitionLoading = true
        delay(300) // Brief delay to smoothen perceived transition
        showTransitionLoading = false
    }

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = {
            AdminBottomBar(navController = adminNavController)
        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = adminNavController,
                startDestination = Screen.AdminDashboard.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Screen.AdminDashboard.route) {
                    AdminDashboardScreen(
                        navController = navController,
                        onNavigateToStudents = {
                            adminNavController.navigate(Screen.Students.route) {
                                popUpTo(adminNavController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        onNavigateToSettings = {
                            adminNavController.navigate(Screen.AdminSettings.route) {
                                popUpTo(adminNavController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        onNavigateToNotifications = {
                            navController.navigate(Screen.Notifications.route)
                        },
                        onNavigateToCourses = {
                            adminNavController.navigate(Screen.ManageCourses.route) {
                                popUpTo(adminNavController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        onNavigateToUsers = {
                            adminNavController.navigate(Screen.UserManagement.route) {
                                popUpTo(adminNavController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        onNavigateToLocalPlayer = {
                            navController.navigate(Screen.LocalPlayer.route)
                        }
                    )
                }
                composable(Screen.StudioSchedule.route) {
                    StudioScheduleScreen(
                        onNavigateBack = {
                            adminNavController.navigate(Screen.AdminDashboard.route) {
                                popUpTo(adminNavController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable(Screen.Students.route) {
                    StudentsScreen(
                        onAddStudentClick = {
                            navController.navigate(Screen.CreateStudent.route)
                        }
                    )
                }
                composable(Screen.AdminSettings.route) {
                    AdminSettingsScreen(
                        onNavigateBack = { adminNavController.popBackStack() },
                        onLogout = {
                            loginViewModel.logout {
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0)
                                }
                            }
                        },
                        onArtistProfileClick = { navController.navigate(Screen.ArtistProfile.route) },
                        onAppMapClick = { navController.navigate(Screen.AppMap.route) }
                    )
                }
                composable(Screen.ManageCourses.route) {
                    ManageCoursesScreen(
                        onCreateCourse = { navController.navigate(Screen.CreateSession.route) }
                    )
                }
                composable(Screen.UserManagement.route) {
                    UserManagementScreen(
                        onNavigateBack = { adminNavController.popBackStack() },
                        onAddUserClick = { navController.navigate(Screen.CreateStudent.route) }
                    )
                }
                composable(Screen.Attendance.route) {
                    AttendanceScreen(
                        onNavigateBack = { adminNavController.popBackStack() }
                    )
                }
            }
            
            // Transition Overlay for perceived smoothness
            AnimatedVisibility(
                visible = showTransitionLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}
