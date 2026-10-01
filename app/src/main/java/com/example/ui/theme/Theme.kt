package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = AwsOrange,
    onPrimary = CloudNavyDark,
    primaryContainer = AwsOrangeDark,
    onPrimaryContainer = AwsOrangeLight,
    secondary = CloudBlue,
    onSecondary = CloudNavyDark,
    secondaryContainer = CloudBlueDark,
    onSecondaryContainer = CloudBlue,
    tertiary = CloudPurple,
    onTertiary = TextPrimary,
    background = CloudNavyDark,
    onBackground = TextPrimary,
    surface = CloudSurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = CloudCardDark,
    onSurfaceVariant = TextSecondary,
    outline = CloudBorderDark,
    outlineVariant = CloudCardElevated,
    error = CloudError,
    onError = TextPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = AwsOrangeDark,
    onPrimary = CloudLightBg,
    primaryContainer = AwsOrangeLight,
    onPrimaryContainer = CloudNavyDark,
    secondary = CloudBlueDark,
    onSecondary = CloudLightBg,
    background = CloudLightBg,
    onBackground = TextLightPrimary,
    surface = CloudLightSurface,
    onSurface = TextLightPrimary,
    surfaceVariant = CloudLightCard,
    onSurfaceVariant = TextLightSecondary,
    outline = CloudLightBorder,
    error = CloudError,
    onError = CloudLightBg
)

@Composable
fun LearnAWSTheme(
    darkTheme: Boolean = true, // Cloud engineers prefer dark mode by default
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
