package cz.mendelu.pef.xchomo.travelsnap.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@OptIn(ExperimentalMaterial3Api::class)
private val DarkColorScheme = darkColorScheme(
    primary = AppColors.DarkPrimaryColor,
    onPrimary = Color.White, // You may want to set this to a color that contrasts with DarkPrimaryColor
    secondary = AppColors.SecondaryTextColor,
    onSecondary = Color.White, // Contrast color for secondary
    tertiary = AppColors.AccentColor,
    onTertiary = Color.White, // Contrast color for tertiary
    background = AppColors.DarkBackground,
    onBackground = AppColors.LightTextColor, // Contrast color for background
    surface = AppColors.DarkPrimaryColor,
    onSurface = Color.White // Contrast color for surface
)

@OptIn(ExperimentalMaterial3Api::class)
private val LightColorScheme = lightColorScheme(
    primary = AppColors.PrimaryColor,
    onPrimary = Color.Black, // Contrast color for primary
    secondary = AppColors.LightPrimaryColor,
    onSecondary = Color.Black, // Contrast color for secondary
    tertiary = AppColors.AccentColor,
    onTertiary = Color.Black, // Contrast color for tertiary
    background = AppColors.LightBackground,
    onBackground = AppColors.DarkTextColor, // Contrast color for background
    surface = AppColors.LightPrimaryColor,
    onSurface = Color.Black // Contrast color for surface
)

@Composable
fun TravelSnapTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CustomTypography,
        content = content
    )
}
