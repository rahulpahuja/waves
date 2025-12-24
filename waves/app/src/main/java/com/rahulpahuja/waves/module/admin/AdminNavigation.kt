package com.rahulpahuja.waves.module.admin

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rahulpahuja.waves.module.schedule.StudioScheduleScreen
import com.rahulpahuja.waves.ui.navigation.Screen

@Composable
fun AdminNavigation(navController: NavController) {
    val adminNavController = rememberNavController()
    val backgroundColor = Color(0xFF10141D)
    val viewModel: AdminDashboardViewModel = hiltViewModel()
    val adminDashboardUiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = {
            AdminBottomBar(navController = adminNavController)
        }
    ) { paddingValues ->
        NavHost(
            navController = adminNavController,
            startDestination = Screen.AdminDashboard.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.AdminDashboard.route) {
                AdminDashboardContent(
                    navController = navController,
                    state = adminDashboardUiState
                ) 
            }
            composable(Screen.StudioSchedule.route) {
                StudioScheduleScreen(onNavigateBack = { adminNavController.popBackStack() })
            }
            composable(Screen.Students.route) {
                StudentsScreen(onAddStudentClick = { navController.navigate(Screen.CreateStudent.route) })
            }
            composable(Screen.AdminSettings.route) {
                AdminSettingsScreen(
                    onNavigateBack = { adminNavController.popBackStack() },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0)
                        }
                    },
                    onArtistProfileClick = { navController.navigate(Screen.ArtistProfile.route) }
                )
            }
        }
    }
}
