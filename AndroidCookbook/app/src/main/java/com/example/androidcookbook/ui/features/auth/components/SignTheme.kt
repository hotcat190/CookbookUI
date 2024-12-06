package com.example.androidcookbook.ui.features.auth.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidcookbook.ui.features.auth.components.SignColor.Background

object SignColor {
    val Oval = Color(0xFF4F3423)
    val Background = Color(0xFF251404)
}

@Composable
fun SignTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = lightColorScheme(
        background = Background,
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}