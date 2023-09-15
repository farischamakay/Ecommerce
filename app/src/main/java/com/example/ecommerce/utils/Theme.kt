package com.example.ecommerce.utils

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val isLightColorScheme = lightColorScheme(
    background = Color.White,
    surface = Color.White
)

private val isDarkColorScheme = darkColorScheme(
    background = Color.Black,
    surface = Color.Black
)

@Composable
fun ComposeTheme(
    content: @Composable () -> Unit
) {
    val isDarkTheme : Boolean = isSystemInDarkTheme()
    val colorScheme = when{
        isDarkTheme -> isDarkColorScheme
        else -> isLightColorScheme
    } //LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}