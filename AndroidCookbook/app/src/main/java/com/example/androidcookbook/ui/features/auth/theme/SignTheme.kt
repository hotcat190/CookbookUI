package com.example.androidcookbook.ui.features.auth.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidcookbook.ui.theme.Typography

val LightTitle = Color(52, 108, 151)
val LightText = Color(13, 27, 38)
val LightButtonColor = Color(64, 120, 170)
val LightClickableText = Color(48, 122, 43)
val LightBackground = Color(217, 231, 242)
val LightOval = Color(179, 207, 229)
val LightErrorText = Color(127, 60, 57)
val LightTextOutline = Color(13, 27, 38)
val LightGradientHighest = Color(217, 231, 242)
val LightGradientHigh = Color(184, 211, 233)
val LightGradientLow = Color(99, 161, 209)
val LightGradientLowest = Color(255, 255, 255)

val DarkOval = Color(0xFF4F3423)
val DarkBackground = Color(0xFF251404)

val LightSignLayout = lightColorScheme(
    primary = LightTitle,
    inversePrimary = LightText,
    secondary = LightButtonColor,
    tertiary = LightClickableText,
    background = LightBackground,
    onBackground = LightOval,
    error = LightErrorText,
    outline = LightTextOutline,
    surfaceContainerHigh = LightGradientHigh,
    surfaceContainerHighest = LightGradientHighest,
    surfaceContainerLow = LightGradientLow,
    surfaceContainerLowest = LightGradientLowest,
)

val DarkSignLayout = darkColorScheme(
    background = DarkBackground,
    onBackground = DarkOval,
)

@Composable
fun SignLayoutTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkSignLayout
        else -> LightSignLayout
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}