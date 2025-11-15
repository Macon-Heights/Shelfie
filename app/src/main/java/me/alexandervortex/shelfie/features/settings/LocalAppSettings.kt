package me.alexandervortex.shelfie.features.settings

import androidx.compose.runtime.compositionLocalOf

object LocalAppSettings {

    val fontSize = compositionLocalOf { 16 }
    val padding = compositionLocalOf { 24 }
    val lineHeight = compositionLocalOf { 1f }
    val theme = compositionLocalOf { 0 }
}