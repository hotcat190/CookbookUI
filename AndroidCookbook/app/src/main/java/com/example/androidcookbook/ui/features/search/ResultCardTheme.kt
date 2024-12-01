package com.example.androidcookbook.ui.features.search

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidcookbook.ui.theme.Typography

val ResultCardLightSurfaceContainer = Color(0xFFFFF7D1)
val ResultCardLightOutline = Color(0xFFFFD09B)

val ResultCardDarkSurfaceContainer = Color(0xFF7C7C7C)
val ResultCardDarkOutline = Color(0xFFFFFFFF)

private val ResultCardLight = lightColorScheme(
    surfaceContainer = ResultCardLightSurfaceContainer,
    outline = ResultCardLightOutline,
)

private val ResultCardDark = darkColorScheme(
//    surfaceContainer = ResultCardDarkSurfaceContainer,
//    outline = ResultCardDarkOutline,
)

@Composable
fun ResultCardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> ResultCardDark
        else -> ResultCardLight
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}