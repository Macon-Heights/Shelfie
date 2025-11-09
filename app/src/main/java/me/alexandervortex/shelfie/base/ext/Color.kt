package me.alexandervortex.shelfie.base.ext

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun getColors(): ColorScheme {
    return MaterialTheme.colorScheme
}

@Composable
fun getStaticSurfaceVariant() = if (isSystemInDarkTheme()) {
    getColors().surfaceVariant
} else {
    getColors().onSurfaceVariant
}