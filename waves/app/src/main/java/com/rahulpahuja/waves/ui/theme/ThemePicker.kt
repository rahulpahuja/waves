package com.rahulpahuja.waves.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ThemePicker(themeVM: ThemeViewModel = hiltViewModel()) {
    val currentTheme by themeVM.currentTheme.collectAsState()
    val role by themeVM.userRole.collectAsState()
    val themes = AppTheme.forRole(role)

    ThemePickerContent(
        themes = themes,
        currentTheme = currentTheme,
        onThemeSelected = { themeVM.setTheme(it) }
    )
}

@Composable
fun ThemePickerContent(
    themes: List<AppTheme>,
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit
) {
    Column {
        Text(
            "APPEARANCE",
            color = Color.Gray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            items(themes) { theme ->
                ThemeChip(
                    theme = theme,
                    selected = theme == currentTheme,
                    onClick = { onThemeSelected(theme) }
                )
            }
        }
    }
}

@Composable
private fun ThemeChip(theme: AppTheme, selected: Boolean, onClick: () -> Unit) {
    val scheme = theme.colorScheme()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(scheme.background)
                .then(
                    if (selected) Modifier.border(2.5.dp, scheme.primary, CircleShape)
                    else Modifier.border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(0.45f)
                    .clip(CircleShape)
                    .background(scheme.primary)
            )
            if (selected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            theme.displayName,
            color = if (selected) Color.White else Color.Gray,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
