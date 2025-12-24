package com.rahulpahuja.waves.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object ForgotPassword : Screen("forgot_password")
    object Home : Screen("home")
    object Notifications : Screen("notifications")
    
    // Onboarding
    object Welcome : Screen("welcome")
    object TrackProgress : Screen("track_progress")
    object CreatePersona : Screen("create_persona")
    object AllSet : Screen("all_set")
    
    // Admin Module
    object AdminDashboard : Screen("admin_dashboard")
    object AdminSettings : Screen("admin_settings")
    object Students : Screen("students")
    object CreateStudent : Screen("create_student")
    object NewCashEntry : Screen("new_cash_entry")
    object CreateSession : Screen("create_session")
    object ManageBookings : Screen("manage_bookings")
    object HelpSupport : Screen("help_support")
    
    // Student Module
    object StudentDashboard : Screen("student_dashboard")
    object StudentSettings : Screen("student_settings")
    object PaymentHistory : Screen("payment_history")
    object ArtistProfile : Screen("artist_profile")
    
    // Shared / Common
    object Chat : Screen("chat")
    object MediaGallery : Screen("media_gallery")
    object Attendance : Screen("attendance")
    object Settings : Screen("settings")
    object ProfileSettings : Screen("profile_settings")
    
    // Radar / Public
    object ArtistRadar : Screen("artist_radar")
    object PublicArtistProfile : Screen("public_artist_profile")
    
    // Schedule
    object StudioSchedule : Screen("studio_schedule")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
