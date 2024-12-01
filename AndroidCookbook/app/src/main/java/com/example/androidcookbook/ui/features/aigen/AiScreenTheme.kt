package com.example.androidcookbook.ui.features.aigen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidcookbook.ui.theme.Typography

val AiLightSurfaceContainer = Color(0xFFF9F4F1)
val AiLightOutline = Color(0xFF7F5346)

val AiDarkSurfaceContainer = Color(0xFF7C7C7C)

private val AiLight = lightColorScheme(
    surfaceContainer = AiLightSurfaceContainer,
    outline = AiLightOutline,
)

private val AiDark = darkColorScheme(
    surfaceContainer = AiDarkSurfaceContainer,
)

@Composable
fun AiScreenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> AiDark
        else -> AiLight
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}