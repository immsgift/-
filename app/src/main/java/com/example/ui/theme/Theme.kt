package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldLight,
    onPrimary = Color.Black,
    primaryContainer = EmeraldDark,
    onPrimaryContainer = EmeraldContainer,
    secondary = GoldAccent,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF4A3700),
    onSecondaryContainer = GoldLight,
    background = ParchmentDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceCardDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFF223129),
    onSurfaceVariant = TextSecondaryDark,
    outline = SoftBorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color.White,
    primaryContainer = EmeraldContainer,
    onPrimaryContainer = OnEmeraldContainer,
    secondary = GoldAccent,
    onSecondary = Color.White,
    secondaryContainer = GoldContainer,
    onSecondaryContainer = OnGoldContainer,
    background = ParchmentLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceCardLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFEEF5F0),
    onSurfaceVariant = TextSecondaryLight,
    outline = SoftBorderLight
)

@Composable
fun QuranKarimTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
