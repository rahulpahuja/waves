package com.rahulpahuja.waves.ui.navigation

/**
 * Screen Routes as constants for faster lookup and consistency
 */
object Route {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val FORGOT_PASSWORD = "forgot_password"
    const val ROLE_SELECTION = "role_selection"
    const val WAITING_APPROVAL = "waiting_approval"
    const val PENDING_APPROVALS = "pending_approvals"

    const val WELCOME = "welcome"
    const val TRACK_PROGRESS = "track_progress"
    const val CREATE_PERSONA = "create_persona"
    const val ALL_SET = "all_set"
    const val WELCOME_TO_THE_BOOTH = "welcome_to_the_booth"
    const val MANAGE_STUDENT_LIFECYCLE = "manage_student_lifecycle"
    const val PROFILE_SETUP = "profile_setup"
    const val ADMIN_WELCOME = "admin_welcome"

    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val ADMIN_SETTINGS = "admin_settings"
    const val STUDENTS = "students"
    const val CREATE_STUDENT = "create_student"
    const val NEW_CASH_ENTRY = "new_cash_entry"
    const val CREATE_SESSION = "create_session"
    const val MANAGE_BOOKINGS = "manage_bookings"
    const val HELP_SUPPORT = "help_support"
    const val ATTENDANCE = "attendance"
    const val PROFILE_SETTINGS = "profile_settings"
    const val MANAGE_COURSES = "manage_courses"
    const val VERIFY_PAYMENTS = "verify_payments"
    const val USER_MANAGEMENT = "user_management"
    const val APP_MAP = "app_map"

    const val STUDENT_DASHBOARD = "student_dashboard"
    const val STUDENT_SETTINGS = "student_settings"
    const val PAYMENT_HISTORY = "payment_history"
    const val ARTIST_PROFILE = "artist_profile"

    const val HOME = "home"
    const val NOTIFICATIONS = "notifications"
    const val CHAT = "chat"
    const val MEDIA_GALLERY = "media_gallery"
    const val SETTINGS = "settings"
    const val ARTIST_RADAR = "artist_radar"
    const val PUBLIC_ARTIST_PROFILE = "public_artist_profile"
    const val STUDIO_SCHEDULE = "studio_schedule"
    const val LOCAL_PLAYER = "local_player"
}

/**
 * Categorized Screen Groups
 */
enum class ScreenGroup {
    AUTH, ONBOARDING, ADMIN, STUDENT, SHARED
}

/**
 * Sealed class for type-safe navigation
 */
sealed class Screen(val route: String, val group: ScreenGroup) {
    // Auth & Splash
    object Splash : Screen(Route.SPLASH, ScreenGroup.AUTH)
    object Login : Screen(Route.LOGIN, ScreenGroup.AUTH)
    object ForgotPassword : Screen(Route.FORGOT_PASSWORD, ScreenGroup.AUTH)
    object RoleSelection : Screen(Route.ROLE_SELECTION, ScreenGroup.AUTH)
    object WaitingApproval : Screen(Route.WAITING_APPROVAL, ScreenGroup.AUTH)
    object PendingApprovals : Screen(Route.PENDING_APPROVALS, ScreenGroup.AUTH)

    // Onboarding
    object Welcome : Screen(Route.WELCOME, ScreenGroup.ONBOARDING)
    object TrackProgress : Screen(Route.TRACK_PROGRESS, ScreenGroup.ONBOARDING)
    object CreatePersona : Screen(Route.CREATE_PERSONA, ScreenGroup.ONBOARDING)
    object AllSet : Screen(Route.ALL_SET, ScreenGroup.ONBOARDING)
    object WelcomeToTheBooth : Screen(Route.WELCOME_TO_THE_BOOTH, ScreenGroup.ONBOARDING)
    object ManageStudentLifecycle : Screen(Route.MANAGE_STUDENT_LIFECYCLE, ScreenGroup.ONBOARDING)
    object ProfileSetup : Screen(Route.PROFILE_SETUP, ScreenGroup.ONBOARDING)
    object AdminWelcome : Screen(Route.ADMIN_WELCOME, ScreenGroup.ONBOARDING)

    // Admin
    object AdminDashboard : Screen(Route.ADMIN_DASHBOARD, ScreenGroup.ADMIN)
    object AdminSettings : Screen(Route.ADMIN_SETTINGS, ScreenGroup.ADMIN)
    object Students : Screen(Route.STUDENTS, ScreenGroup.ADMIN)
    object CreateStudent : Screen(Route.CREATE_STUDENT, ScreenGroup.ADMIN)
    object NewCashEntry : Screen(Route.NEW_CASH_ENTRY, ScreenGroup.ADMIN)
    object CreateSession : Screen(Route.CREATE_SESSION, ScreenGroup.ADMIN)
    object ManageBookings : Screen(Route.MANAGE_BOOKINGS, ScreenGroup.ADMIN)
    object HelpSupport : Screen(Route.HELP_SUPPORT, ScreenGroup.ADMIN)
    object Attendance : Screen(Route.ATTENDANCE, ScreenGroup.ADMIN)
    object ProfileSettings : Screen(Route.PROFILE_SETTINGS, ScreenGroup.ADMIN)
    object ManageCourses : Screen(Route.MANAGE_COURSES, ScreenGroup.ADMIN)
    object VerifyPayments : Screen(Route.VERIFY_PAYMENTS, ScreenGroup.ADMIN)
    object UserManagement : Screen(Route.USER_MANAGEMENT, ScreenGroup.ADMIN)
    object AppMap : Screen(Route.APP_MAP, ScreenGroup.SHARED)

    // Student
    object StudentDashboard : Screen(Route.STUDENT_DASHBOARD, ScreenGroup.STUDENT)
    object StudentSettings : Screen(Route.STUDENT_SETTINGS, ScreenGroup.STUDENT)
    object PaymentHistory : Screen(Route.PAYMENT_HISTORY, ScreenGroup.STUDENT)
    object ArtistProfile : Screen(Route.ARTIST_PROFILE, ScreenGroup.STUDENT)

    // Shared
    object Home : Screen(Route.HOME, ScreenGroup.SHARED)
    object Notifications : Screen(Route.NOTIFICATIONS, ScreenGroup.SHARED)
    object Chat : Screen(Route.CHAT, ScreenGroup.SHARED)
    object MediaGallery : Screen(Route.MEDIA_GALLERY, ScreenGroup.SHARED)
    object Settings : Screen(Route.SETTINGS, ScreenGroup.SHARED)
    object ArtistRadar : Screen(Route.ARTIST_RADAR, ScreenGroup.SHARED)
    object PublicArtistProfile : Screen(Route.PUBLIC_ARTIST_PROFILE, ScreenGroup.SHARED)
    object StudioSchedule : Screen(Route.STUDIO_SCHEDULE, ScreenGroup.SHARED)
    object LocalPlayer : Screen(Route.LOCAL_PLAYER, ScreenGroup.SHARED)
}
