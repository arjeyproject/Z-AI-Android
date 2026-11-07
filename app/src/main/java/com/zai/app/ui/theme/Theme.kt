package com.zai.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkScheme = darkColorScheme(
    primary = Color(0xFFEDEDF2),
    onPrimary = Color(0xFF0B0B0D),
    primaryContainer = Color(0xFF1D1D23),
    onPrimaryContainer = Color(0xFFE8E8EE),
    secondary = Color(0xFF9AA0AC),
    onSecondary = Color(0xFF101216),
    background = Color(0xFF0B0B0D),
    onBackground = Color(0xFFE8E8EE),
    surface = Color(0xFF0B0B0D),
    onSurface = Color(0xFFE8E8EE),
    surfaceVariant = Color(0xFF17171C),
    onSurfaceVariant = Color(0xFFB8BAC4),
    outline = Color(0xFF2C2C34),
    error = Color(0xFFFF6B6B)
)

private val LightScheme = lightColorScheme(
    primary = Color(0xFF141419),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE9E9EF),
    onPrimaryContainer = Color(0xFF1A1A20),
    secondary = Color(0xFF5A5E6A),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFFAFAFC),
    onBackground = Color(0xFF1A1A20),
    surface = Color(0xFFFAFAFC),
    onSurface = Color(0xFF1A1A20),
    surfaceVariant = Color(0xFFF0F0F4),
    onSurfaceVariant = Color(0xFF4A4E58),
    outline = Color(0xFFD8D8E0),
    error = Color(0xFFC0392B)
)

@Composable
fun ZaiTheme(themeMode: String, content: @Composable () -> Unit) {
    val dark = when (themeMode) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
    MaterialTheme(
        colorScheme = if (dark) DarkScheme else LightScheme,
        content = content
    )
}
