package com.rahulpahuja.waves.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

enum class AppTheme(val displayName: String, val role: String, val primary: Color) {

    // ── Admin ──────────────────────────────────────────────────────────────
    ADMIN_SLATE    ("Slate",    "admin", Color(0xFF2962FF)),
    ADMIN_CARBON   ("Carbon",   "admin", Color(0xFF00E676)),
    ADMIN_SAPPHIRE ("Sapphire", "admin", Color(0xFFFFD700)),
    ADMIN_GRAPHITE ("Graphite", "admin", Color(0xFFFF1744)),
    ADMIN_OBSIDIAN ("Obsidian", "admin", Color(0xFF9C27B0)),

    // ── Student ────────────────────────────────────────────────────────────
    STUDENT_OCEAN  ("Ocean",  "student", Color(0xFF00BCD4)),
    STUDENT_SUNSET ("Sunset", "student", Color(0xFFFF6D00)),
    STUDENT_FOREST ("Forest", "student", Color(0xFF66BB6A)),
    STUDENT_ROSE   ("Rose",   "student", Color(0xFFE91E63)),
    STUDENT_AURORA ("Aurora", "student", Color(0xFF7C4DFF)),

    // ── DJ ─────────────────────────────────────────────────────────────────
    DJ_NEON   ("Neon",   "dj", Color(0xFFE040FB)),
    DJ_RAVE   ("Rave",   "dj", Color(0xFF00E5FF)),
    DJ_FIRE   ("Fire",   "dj", Color(0xFFFF3D00)),
    DJ_BASS   ("Bass",   "dj", Color(0xFFFFD600)),
    DJ_CHROME ("Chrome", "dj", Color(0xFFB0BEC5));

    fun colorScheme(): ColorScheme = when (this) {
        ADMIN_SLATE     -> darkColorScheme(primary = primary, onPrimary = Color.White, background = Color(0xFF10141D), onBackground = Color.White, surface = Color(0xFF1E232F), onSurface = Color.White, surfaceVariant = Color(0xFF2C3240), onSurfaceVariant = Color(0xFFB0B8C8), outline = Color(0xFF3A4150), error = Color(0xFFFF5252), onError = Color.White, primaryContainer = Color(0xFF1E3A8A), onPrimaryContainer = Color.White, secondary = Color(0xFF1E232F), onSecondary = Color.White)
        ADMIN_CARBON    -> darkColorScheme(primary = primary, onPrimary = Color.Black, background = Color(0xFF0A0A0A), onBackground = Color.White, surface = Color(0xFF1A1A1A), onSurface = Color.White, surfaceVariant = Color(0xFF252525), onSurfaceVariant = Color(0xFFAAAAAA), outline = Color(0xFF333333), error = Color(0xFFFF5252), onError = Color.White, primaryContainer = Color(0xFF003D20), onPrimaryContainer = Color.White, secondary = Color(0xFF1A1A1A), onSecondary = Color.White)
        ADMIN_SAPPHIRE  -> darkColorScheme(primary = primary, onPrimary = Color.Black, background = Color(0xFF0D1B3E), onBackground = Color.White, surface = Color(0xFF1A2A56), onSurface = Color.White, surfaceVariant = Color(0xFF1F3060), onSurfaceVariant = Color(0xFFB0BEC5), outline = Color(0xFF2A3F70), error = Color(0xFFFF5252), onError = Color.White, primaryContainer = Color(0xFF5A4000), onPrimaryContainer = Color.White, secondary = Color(0xFF1A2A56), onSecondary = Color.White)
        ADMIN_GRAPHITE  -> darkColorScheme(primary = primary, onPrimary = Color.White, background = Color(0xFF1A1A1A), onBackground = Color.White, surface = Color(0xFF2A2A2A), onSurface = Color.White, surfaceVariant = Color(0xFF333333), onSurfaceVariant = Color(0xFFAAAAAA), outline = Color(0xFF444444), error = Color(0xFFFF5252), onError = Color.White, primaryContainer = Color(0xFF7F0000), onPrimaryContainer = Color.White, secondary = Color(0xFF2A2A2A), onSecondary = Color.White)
        ADMIN_OBSIDIAN  -> darkColorScheme(primary = primary, onPrimary = Color.White, background = Color(0xFF0D0D17), onBackground = Color.White, surface = Color(0xFF1A1A28), onSurface = Color.White, surfaceVariant = Color(0xFF232330), onSurfaceVariant = Color(0xFFB0AABF), outline = Color(0xFF33303F), error = Color(0xFFFF5252), onError = Color.White, primaryContainer = Color(0xFF4A1060), onPrimaryContainer = Color.White, secondary = Color(0xFF1A1A28), onSecondary = Color.White)

        STUDENT_OCEAN   -> darkColorScheme(primary = primary, onPrimary = Color.Black, background = Color(0xFF071523), onBackground = Color.White, surface = Color(0xFF102030), onSurface = Color.White, surfaceVariant = Color(0xFF162A3D), onSurfaceVariant = Color(0xFFAAC8D8), outline = Color(0xFF1E3A50), error = Color(0xFFFF5252), onError = Color.White, primaryContainer = Color(0xFF004D5E), onPrimaryContainer = Color.White, secondary = Color(0xFF102030), onSecondary = Color.White)
        STUDENT_SUNSET  -> darkColorScheme(primary = primary, onPrimary = Color.White, background = Color(0xFF1A0D00), onBackground = Color.White, surface = Color(0xFF2A1800), onSurface = Color.White, surfaceVariant = Color(0xFF3A2200), onSurfaceVariant = Color(0xFFD8B090), outline = Color(0xFF4A2E00), error = Color(0xFFFF5252), onError = Color.White, primaryContainer = Color(0xFF7F3600), onPrimaryContainer = Color.White, secondary = Color(0xFF2A1800), onSecondary = Color.White)
        STUDENT_FOREST  -> darkColorScheme(primary = primary, onPrimary = Color.Black, background = Color(0xFF071A0A), onBackground = Color.White, surface = Color(0xFF102815), onSurface = Color.White, surfaceVariant = Color(0xFF16341C), onSurfaceVariant = Color(0xFFAAC8AA), outline = Color(0xFF1E4224), error = Color(0xFFFF5252), onError = Color.White, primaryContainer = Color(0xFF1B5E20), onPrimaryContainer = Color.White, secondary = Color(0xFF102815), onSecondary = Color.White)
        STUDENT_ROSE    -> darkColorScheme(primary = primary, onPrimary = Color.White, background = Color(0xFF1A0A14), onBackground = Color.White, surface = Color(0xFF281020), onSurface = Color.White, surfaceVariant = Color(0xFF34162A), onSurfaceVariant = Color(0xFFD8A0C0), outline = Color(0xFF421C34), error = Color(0xFFFF5252), onError = Color.White, primaryContainer = Color(0xFF7F0030), onPrimaryContainer = Color.White, secondary = Color(0xFF281020), onSecondary = Color.White)
        STUDENT_AURORA  -> darkColorScheme(primary = primary, onPrimary = Color.White, background = Color(0xFF0D0A1A), onBackground = Color.White, surface = Color(0xFF1A1528), onSurface = Color.White, surfaceVariant = Color(0xFF231E35), onSurfaceVariant = Color(0xFFBAB0D8), outline = Color(0xFF302844), error = Color(0xFFFF5252), onError = Color.White, primaryContainer = Color(0xFF3D007F), onPrimaryContainer = Color.White, secondary = Color(0xFF1A1528), onSecondary = Color.White)

        DJ_NEON         -> darkColorScheme(primary = primary, onPrimary = Color.Black, background = Color(0xFF08000F), onBackground = Color.White, surface = Color(0xFF14001F), onSurface = Color.White, surfaceVariant = Color(0xFF1E0030), onSurfaceVariant = Color(0xFFD0A0E0), outline = Color(0xFF2A0040), error = Color(0xFFFF5252), onError = Color.White, primaryContainer = Color(0xFF6A007F), onPrimaryContainer = Color.White, secondary = Color(0xFF14001F), onSecondary = Color.White)
        DJ_RAVE         -> darkColorScheme(primary = primary, onPrimary = Color.Black, background = Color(0xFF000A1A), onBackground = Color.White, surface = Color(0xFF001428), onSurface = Color.White, surfaceVariant = Color(0xFF001E38), onSurfaceVariant = Color(0xFFA0C8E0), outline = Color(0xFF002848), error = Color(0xFFFF5252), onError = Color.White, primaryContainer = Color(0xFF006080), onPrimaryContainer = Color.White, secondary = Color(0xFF001428), onSecondary = Color.White)
        DJ_FIRE         -> darkColorScheme(primary = primary, onPrimary = Color.White, background = Color(0xFF1A0500), onBackground = Color.White, surface = Color(0xFF2A0A00), onSurface = Color.White, surfaceVariant = Color(0xFF3A1000), onSurfaceVariant = Color(0xFFE0A090), outline = Color(0xFF4A1800), error = Color(0xFFFF5252), onError = Color.White, primaryContainer = Color(0xFF7F1900), onPrimaryContainer = Color.White, secondary = Color(0xFF2A0A00), onSecondary = Color.White)
        DJ_BASS         -> darkColorScheme(primary = primary, onPrimary = Color.Black, background = Color(0xFF0F0F00), onBackground = Color.White, surface = Color(0xFF1E1E00), onSurface = Color.White, surfaceVariant = Color(0xFF2C2C00), onSurfaceVariant = Color(0xFFD8D890), outline = Color(0xFF3A3A00), error = Color(0xFFFF5252), onError = Color.White, primaryContainer = Color(0xFF7F6C00), onPrimaryContainer = Color.Black, secondary = Color(0xFF1E1E00), onSecondary = Color.White)
        DJ_CHROME       -> darkColorScheme(primary = primary, onPrimary = Color.Black, background = Color(0xFF111318), onBackground = Color.White, surface = Color(0xFF1E2128), onSurface = Color.White, surfaceVariant = Color(0xFF2A2D35), onSurfaceVariant = Color(0xFFB8BCC8), outline = Color(0xFF383B44), error = Color(0xFFFF5252), onError = Color.White, primaryContainer = Color(0xFF546E7A), onPrimaryContainer = Color.White, secondary = Color(0xFF1E2128), onSecondary = Color.White)
    }

    companion object {
        fun forRole(role: String) = entries.filter { it.role == role }
        fun fromKey(key: String) = entries.firstOrNull { it.name == key } ?: ADMIN_SLATE
        fun defaultForRole(role: String) = when (role) {
            "admin"   -> ADMIN_SLATE
            "student" -> STUDENT_OCEAN
            "dj"      -> DJ_NEON
            else      -> ADMIN_SLATE
        }
    }
}
