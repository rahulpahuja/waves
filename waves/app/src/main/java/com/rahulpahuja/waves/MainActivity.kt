package com.rahulpahuja.waves

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rahulpahuja.waves.module.admin.AdminDashboardScreen
import com.rahulpahuja.waves.module.admin.AdminSettingsScreen
import com.rahulpahuja.waves.module.admin.ArtistProfileScreen
import com.rahulpahuja.waves.module.admin.AttendanceScreen
import com.rahulpahuja.waves.module.admin.CreateSessionScreen
import com.rahulpahuja.waves.module.admin.CreateStudentScreen
import com.rahulpahuja.waves.module.admin.HelpSupportScreen
import com.rahulpahuja.waves.module.admin.NewCashEntryScreen
import com.rahulpahuja.waves.module.admin.PaymentHistoryScreen
import com.rahulpahuja.waves.module.admin.ProfileSettingsScreen
import com.rahulpahuja.waves.module.admin.SettingsScreen
import com.rahulpahuja.waves.module.admin.StudentsScreen
import com.rahulpahuja.waves.module.auth.ForgotPasswordScreen
import com.rahulpahuja.waves.module.auth.LoginScreen
import com.rahulpahuja.waves.module.chat.ChatScreen
import com.rahulpahuja.waves.module.gallery.MediaGalleryScreen
import com.rahulpahuja.waves.module.onboarding.* 
import com.rahulpahuja.waves.module.radar.ArtistRadarScreen
import com.rahulpahuja.waves.module.radar.PublicArtistProfileScreen
import com.rahulpahuja.waves.module.schedule.ManageBookingsScreen
import com.rahulpahuja.waves.module.schedule.StudioScheduleScreen
import com.rahulpahuja.waves.module.splash.SplashScreen
import com.rahulpahuja.waves.module.student.StudentDashboardScreen
import com.rahulpahuja.waves.module.student.StudentSettingsScreen
import com.rahulpahuja.waves.ui.components.NotificationsScreen
import com.rahulpahuja.waves.ui.home.HomeScreen
import com.rahulpahuja.waves.ui.navigation.Screen
import com.rahulpahuja.waves.ui.theme.WavesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WavesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController, 
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        // Onboarding
        composable(Screen.Welcome.route) {
            WelcomeScreen(onGetStartedClick = { navController.navigate(Screen.TrackProgress.route) })
        }
        composable(Screen.TrackProgress.route) {
            TrackProgressScreen(
                onNextClick = { navController.navigate(Screen.CreatePersona.route) },
                onSkipClick = { navController.navigate(Screen.StudentDashboard.route) }
            )
        }
        composable(Screen.CreatePersona.route) {
            CreatePersonaScreen(
                onContinueClick = { navController.navigate(Screen.AllSet.route) },
                onLaterClick = { navController.navigate(Screen.StudentDashboard.route) },
                onNavigateBack = { 
                    navController.popBackStack() 
                }
            )
        }
        composable(Screen.AllSet.route) {
            AllSetScreen(onStartLearningClick = {
                navController.navigate(Screen.StudentDashboard.route) {
                    popUpTo(Screen.Welcome.route) { inclusive = true }
                }
            })
        }
        composable(Screen.WelcomeToTheBooth.route) {
            WelcomeToTheBoothScreen(onStartManagingClick = { navController.navigate(Screen.ManageStudentLifecycle.route) })
        }
        composable(Screen.ManageStudentLifecycle.route) {
            ManageStudentLifecycleScreen(
                onNextClick = { navController.navigate(Screen.ProfileSetup.route) },
                onSkipClick = { navController.navigate(Screen.AdminDashboard.route) }
            )
        }
        composable(Screen.ProfileSetup.route) {
            ProfileSetupScreen(
                onContinueClick = { navController.navigate(Screen.AdminWelcome.route) },
                onSkipClick = { navController.navigate(Screen.AdminDashboard.route) }
            )
        }
        composable(Screen.AdminWelcome.route) {
            AdminWelcomeScreen(
                onEnterDashboardClick = { navController.navigate(Screen.AdminDashboard.route) },
                onViewProfileClick = { navController.navigate(Screen.ArtistProfile.route) }
            )
        }

        // Auth
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = { isAdmin ->
                    if (isAdmin) {
                        navController.navigate(Screen.AdminDashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Welcome.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                },
                onForgotPasswordClick = { navController.navigate(Screen.ForgotPassword.route) },
                onSignUpClick = { /* TODO: Navigate to Sign Up */ }
            )
        }
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Home (Generic)
        composable(Screen.Home.route) {
            HomeScreen()
        }

        // Notifications
        composable(Screen.Notifications.route) {
            NotificationsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Admin Module
        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(navController)
        }
        composable(Screen.AdminSettings.route) {
            AdminSettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout =  { navController.popBackStack() },
                onArtistProfileClick = { navController.navigate(Screen.ArtistProfile.route) }
            )

        }
        composable(Screen.Students.route) {
            StudentsScreen(
                onAddStudentClick = { navController.navigate(Screen.CreateStudent.route) }
            )
        }
        composable(Screen.CreateStudent.route) {
            CreateStudentScreen(
                onNavigateBack = { navController.popBackStack() },
                onCompleteEnrollment = { navController.popBackStack() }
            )
        }
        composable(Screen.NewCashEntry.route) {
            NewCashEntryScreen(
                onDismiss = { navController.popBackStack() }
            )
        }
        composable(Screen.CreateSession.route) {
            CreateSessionScreen(
                onNavigateBack = { navController.popBackStack() },
                onPublish = { navController.popBackStack() }
            )
        }
        composable(Screen.ManageBookings.route) {
            ManageBookingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.HelpSupport.route) {
            HelpSupportScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Student Module
        composable(Screen.StudentDashboard.route) {
            StudentDashboardScreen(navController = navController)
        }
        composable(Screen.StudentSettings.route) {
            StudentSettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) // Clear backstack
                    }
                }
            )
        }
        composable(Screen.PaymentHistory.route) {
            PaymentHistoryScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.ArtistProfile.route) {
            ArtistProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Shared / Common
        composable(Screen.Chat.route) {
            ChatScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.MediaGallery.route) {
            MediaGalleryScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHome = { navController.navigate(Screen.StudentDashboard.route) },
                onNavigateToSchedule = { navController.navigate(Screen.StudioSchedule.route) },
                onNavigateToProfile = { navController.navigate(Screen.StudentSettings.route) }
            )
        }
        composable(Screen.Attendance.route) {
            AttendanceScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(Screen.ProfileSettings.route) {
            ProfileSettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        // Radar / Public
        composable(Screen.ArtistRadar.route) {
            ArtistRadarScreen()
        }
        composable(Screen.PublicArtistProfile.route) {
            PublicArtistProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Schedule
        composable(Screen.StudioSchedule.route) {
            StudioScheduleScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WavesTheme {
        AppNavigation()
    }
}
