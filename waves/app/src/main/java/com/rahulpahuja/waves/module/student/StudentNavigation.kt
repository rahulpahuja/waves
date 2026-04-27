package com.rahulpahuja.waves.module.student

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
import com.rahulpahuja.waves.module.auth.login.LoginViewModel
import com.rahulpahuja.waves.module.gallery.mediagallery.MediaGalleryScreen
import com.rahulpahuja.waves.module.schedule.studioschedule.StudioScheduleScreen
import com.rahulpahuja.waves.module.student.studentdashboard.StudentBottomBar
import com.rahulpahuja.waves.module.student.studentdashboard.StudentDashboardScreen
import com.rahulpahuja.waves.module.student.studentsettings.StudentSettingsScreen
import com.rahulpahuja.waves.ui.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun StudentNavigation(navController: NavController) {
    val studentNavController = rememberNavController()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val backgroundColor = MaterialTheme.colorScheme.background

    // Smooth loading transition logic
    val navBackStackEntry by studentNavController.currentBackStackEntryAsState()
    var showTransitionLoading by remember { mutableStateOf(false) }

    LaunchedEffect(navBackStackEntry) {
        showTransitionLoading = true
        delay(300) // Brief delay to smoothen perceived transition
        showTransitionLoading = false
    }

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = {
            StudentBottomBar(navController = studentNavController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = studentNavController,
                startDestination = Screen.StudentDashboard.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Screen.StudentDashboard.route) {
                    StudentDashboardScreen(
                        navController = navController,
                        onNavigateToSchedule = { studentNavController.navigate(Screen.StudioSchedule.route) },
                        onNavigateToLibrary = { studentNavController.navigate(Screen.MediaGallery.route) },
                        onNavigateToProfile = { studentNavController.navigate(Screen.StudentSettings.route) }
                    )
                }
                composable(Screen.StudioSchedule.route) {
                    StudioScheduleScreen(
                        onNavigateBack = {
                            studentNavController.navigate(Screen.StudentDashboard.route) {
                                popUpTo(studentNavController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable(Screen.MediaGallery.route) {
                    MediaGalleryScreen(
                        onNavigateBack = { studentNavController.popBackStack() },
                        onNavigateToHome = {
                            studentNavController.navigate(Screen.StudentDashboard.route) {
                                popUpTo(studentNavController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        onNavigateToSchedule = {
                            studentNavController.navigate(Screen.StudioSchedule.route) {
                                popUpTo(studentNavController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        onNavigateToProfile = {
                            studentNavController.navigate(Screen.StudentSettings.route) {
                                popUpTo(studentNavController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable(Screen.StudentSettings.route) {
                    StudentSettingsScreen(
                        onNavigateBack = { studentNavController.popBackStack() },
                        onLogout = {
                            loginViewModel.logout {
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0)
                                }
                            }
                        },
                        onAppMapClick = { navController.navigate(Screen.AppMap.route) }
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
