package me.alexandervortex.shelfie.features.settings

import androidx.compose.runtime.compositionLocalOf

object LocalAppSettings {

    val fontSize = compositionLocalOf { 16 }
    // todo later
    //    val ttsSpeed = compositionLocalOf { 1f }
    //    val themeMode = compositionLocalOf { ThemeMode.System }
}
