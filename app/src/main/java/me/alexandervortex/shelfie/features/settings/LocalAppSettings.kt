package me.alexandervortex.shelfie.features.settings

import androidx.compose.runtime.compositionLocalOf

object LocalAppSettings {

    val fontSize = compositionLocalOf { 16 }
    val stoppingTime = compositionLocalOf { 0L }
    val lineHeight = compositionLocalOf { 1f }
}
