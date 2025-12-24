//package com.rahulpahuja.waves.module.student
//
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.navigation.NavController
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.rahulpahuja.waves.module.gallery.MediaGalleryScreen
//import com.rahulpahuja.waves.module.schedule.StudioScheduleScreen
//import com.rahulpahuja.waves.ui.navigation.Screen
//
//@Composable
//fun StudentNavigation(
//    navController: NavController
//) {
//    val studentNavController = rememberNavController()
//    val backgroundColor = Color(0xFF10141D)
//
//    Scaffold(
//        containerColor = backgroundColor,
//        bottomBar = {
//            StudentBottomBar(navController = studentNavController)
//        }
//    ) { paddingValues ->
//        NavHost(
//            navController = studentNavController,
//            startDestination = Screen.StudentDashboard.route,
//            modifier = Modifier.padding(paddingValues)
//        ) {
//            composable(Screen.StudentDashboard.route) {
//                DashboardContent(
//                    navController = navController
//                )
//            }
//            composable(Screen.StudioSchedule.route) {
//                StudioScheduleScreen(
//                    onNavigateBack = { studentNavController.popBackStack() }
//                )
//            }
//            composable(Screen.MediaGallery.route) {
//                MediaGalleryScreen(
//                    onNavigateBack = { studentNavController.popBackStack() },
//                    onNavigateToHome = { studentNavController.navigate(Screen.StudentDashboard.route) },
//                    onNavigateToSchedule = { studentNavController.navigate(Screen.StudioSchedule.route) },
//                    onNavigateToProfile = { studentNavController.navigate(Screen.StudentSettings.route) }
//                )
//            }
//            composable(Screen.StudentSettings.route) {
//                StudentSettingsScreen(
//                    onNavigateBack = { studentNavController.popBackStack() },
//                    onLogout = {
//                        navController.navigate(Screen.Login.route) {
//                            popUpTo(0)
//                        }
//                    }
//                )
//            }
//        }
//    }
//}
