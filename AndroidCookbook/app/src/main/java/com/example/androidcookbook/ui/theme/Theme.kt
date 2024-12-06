package com.example.androidcookbook.ui.theme

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
    primary = Color.hsl(hue = 207f, saturation = 0.5f, lightness = 0.1f),
    secondary = Color.hsl(hue = 207f, saturation = 0.5f, lightness = 0.9f),
    tertiary = Color.hsl(hue = 178f, saturation = 0.49f, lightness = 0.1f),
    scrim = Color.hsl(hue = 147f, saturation = 0.8f, lightness = 0.2f), // this is accent
    primaryContainer = Color.hsl(175f, 0.49f, 0.9f),


    onSecondary = Color.hsl(207f, 0.48f, 0.82f),

)

private val LightColorScheme = lightColorScheme(
    primary = Color.hsl(hue = 207f, saturation = 0.5f, lightness = 0.9f),
    secondary = Color.hsl(hue = 207f, saturation = 0.5f, lightness = 0.1f),
    tertiary = Color.hsl(hue = 326f, saturation = 0.49f, lightness = 0.9f),
    scrim = Color.hsl(hue = 147f, saturation = 0.8f, lightness = 0.8f), // this is accent
    primaryContainer = Color.hsl(206f, 0.49f, 0.1f),
    onSecondary = Color.hsl(207f, 0.48f, 0.18f),
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun AndroidCookbookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}