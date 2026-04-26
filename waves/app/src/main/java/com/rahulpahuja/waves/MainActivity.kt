package com.rahulpahuja.waves

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rahulpahuja.waves.module.admin.AdminNavigation
import com.rahulpahuja.waves.module.admin.artistprofile.ArtistProfileScreen
import com.rahulpahuja.waves.module.admin.attendance.AttendanceScreen
import com.rahulpahuja.waves.module.admin.createsession.CreateSessionScreen
import com.rahulpahuja.waves.module.admin.helpsupport.HelpSupportScreen
import com.rahulpahuja.waves.module.admin.newcashentry.NewCashEntryScreen
import com.rahulpahuja.waves.module.admin.paymenthistory.PaymentHistoryScreen
import com.rahulpahuja.waves.module.admin.pendingapprovals.PendingApprovalsScreen
import com.rahulpahuja.waves.module.admin.profilesettings.ProfileSettingsScreen
import com.rahulpahuja.waves.module.admin.settings.SettingsScreen
import com.rahulpahuja.waves.module.admin.verifypayments.VerifyPaymentsScreen
import com.rahulpahuja.waves.module.auth.login.LoginScreen
import com.rahulpahuja.waves.module.auth.login.LoginViewModel
import com.rahulpahuja.waves.module.auth.roleselection.RoleSelectionScreen
import com.rahulpahuja.waves.module.auth.waitingapproval.WaitingApprovalScreen
import com.rahulpahuja.waves.module.chat.chat.ChatScreen
import com.rahulpahuja.waves.module.gallery.mediagallery.MediaGalleryScreen
import com.rahulpahuja.waves.module.onboarding.allset.AllSetScreen
import com.rahulpahuja.waves.module.onboarding.createpersona.CreatePersonaScreen
import com.rahulpahuja.waves.module.onboarding.trackprogress.TrackProgressScreen
import com.rahulpahuja.waves.module.onboarding.welcome.WelcomeScreen
import com.rahulpahuja.waves.module.radar.artistradar.ArtistRadarScreen
import com.rahulpahuja.waves.module.radar.publicartistprofile.PublicArtistProfileScreen
import com.rahulpahuja.waves.module.schedule.managebookings.ManageBookingsScreen
import com.rahulpahuja.waves.module.schedule.studioschedule.StudioScheduleScreen
import com.rahulpahuja.waves.module.splash.splash.SplashScreen
import com.rahulpahuja.waves.module.student.StudentNavigation
import com.rahulpahuja.waves.ui.components.NotificationsScreen
import com.rahulpahuja.waves.ui.home.HomeScreen
import com.rahulpahuja.waves.ui.navigation.Screen
import com.rahulpahuja.waves.ui.theme.WavesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        askNotificationPermission()
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

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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

        // Auth & Approval Flow
        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                onLoginClick = { isAdmin ->
                    val destination = if (isAdmin) Screen.AdminDashboard.route else Screen.Welcome.route
                    navController.navigate(destination) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onForgotPasswordClick = { navController.navigate(Screen.ForgotPassword.route) },
                onSignUpClick = { /* TODO: Navigate to Sign Up */ }
            )
        }
        composable(Screen.RoleSelection.route) {
            RoleSelectionScreen(navController = navController)
        }
        composable(Screen.WaitingApproval.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            WaitingApprovalScreen(
                onLogout = {
                    loginViewModel.logout {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0)
                        }
                    }
                },
                onApproved = { isAdmin ->
                    val destination = if (isAdmin) Screen.AdminDashboard.route else Screen.Welcome.route
                    navController.navigate(destination) {
                        popUpTo(Screen.WaitingApproval.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.PendingApprovals.route) {
            PendingApprovalsScreen(onNavigateBack = { navController.popBackStack() })
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
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.AllSet.route) {
            AllSetScreen(onStartLearningClick = {
                navController.navigate(Screen.StudentDashboard.route) {
                    popUpTo(Screen.Welcome.route) { inclusive = true }
                }
            })
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
            AdminNavigation(navController)
        }
        composable(Screen.NewCashEntry.route) {
            NewCashEntryScreen(onDismiss = { navController.popBackStack() })
        }
        composable(Screen.CreateSession.route) {
            CreateSessionScreen(
                onNavigateBack = { navController.popBackStack() },
                onPublish = { navController.popBackStack() }
            )
        }
        composable(Screen.ManageBookings.route) {
            ManageBookingsScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.VerifyPayments.route) {
            VerifyPaymentsScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.HelpSupport.route) {
            HelpSupportScreen(onNavigateBack = { navController.popBackStack() })
        }

        // Student Module
        composable(Screen.StudentDashboard.route) {
            StudentNavigation(navController = navController)
        }
        composable(Screen.PaymentHistory.route) {
            PaymentHistoryScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.ArtistProfile.route) {
            ArtistProfileScreen(onNavigateBack = { navController.popBackStack() })
        }

        // Shared / Common
        composable(Screen.Chat.route) {
            ChatScreen(onNavigateBack = { navController.popBackStack() })
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
            AttendanceScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    loginViewModel.logout {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0)
                        }
                    }
                }
            )
        }
        composable(Screen.ProfileSettings.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            ProfileSettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    loginViewModel.logout {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0)
                        }
                    }
                }
            )
        }

        // Radar / Public
        composable(Screen.ArtistRadar.route) {
            ArtistRadarScreen()
        }
        composable(Screen.PublicArtistProfile.route) {
            PublicArtistProfileScreen(onNavigateBack = { navController.popBackStack() })
        }

        // Schedule
        composable(Screen.StudioSchedule.route) {
            StudioScheduleScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
