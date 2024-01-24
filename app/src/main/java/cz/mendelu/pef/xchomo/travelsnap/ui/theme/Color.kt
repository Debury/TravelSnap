package cz.mendelu.pef.xchomo.travelsnap.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder

object AppColors {
    // Using the shades from your palette
    val DarkPrimaryColor = Color(0xFFB85511)
    val LightPrimaryColor = Color(0xFFFFB27E)
    val PrimaryColor = Color(0xFFE27429)
    val TextIconsColor = Color(0xFF212121)
    val AccentColor = Color(0xFFF39352)
    val PrimaryTextColor = Color(0xFF212121)
    val LightBackground = Color(0xFFFFFFFF)
    val DarkBackground = Color(0xFF000000)
    val LightTextColor = Color(0xFF000000)
    val DarkTextColor = Color(0xFFFFFFFF)
    val SecondaryTextColor = Color(0xFF757575)
    val DividerColor = Color(0xFFBDBDBD)
    val PlaceholderTextColor = Color(0xFF923B00)
    val BackgroundColor = Color(0xFFFFE6DA)

    @Composable
    fun getBackgroundColor() = if (isSystemInDarkTheme()) DarkBackground else LightBackground

    @Composable
    fun basicTextColor(): Color = if (isSystemInDarkTheme()) DarkTextColor else LightTextColor

    @Composable
    fun getTintColor() = if (isSystemInDarkTheme()) Color.White else Color.Black
}