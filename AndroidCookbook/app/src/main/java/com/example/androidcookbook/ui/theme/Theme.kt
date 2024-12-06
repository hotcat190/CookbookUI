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
//    primary = Dark.Primary,
//    secondary = Dark.Secondary,
//    tertiary = Dark.Tertiary,
//    scrim = Dark.Scrim, // this is accent
//    primaryContainer = Dark.PrimaryContainer,
////    primaryContainer = Color.Transparent,
//    onSecondary = Dark.OnSecondary,

)

private val LightColorScheme = lightColorScheme(
//    primary = Light.Primary,
//    secondary = Light.Secondary,
//    tertiary = Light.Tertiary,
//    scrim = Light.Scrim, // this is accent
////    primaryContainer = Light.PrimaryContainer,
//    primaryContainer = Color.Transparent,
//    onSecondary = Light.OnSecondary,
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
)

@Composable
fun AndroidCookbookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
//        darkTheme -> darkColorScheme()
//        else -> lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}