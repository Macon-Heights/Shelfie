package me.alexandervortex.shelfie.feature.settings

import me.alexandervortex.shelfie.feature.settings.values.ThemeValue

sealed interface SettingsIntent {
    data class ChangeFont(val value: Int = 16) : SettingsIntent
    data class ChangeLineHeight(val value: Float = 1f) : SettingsIntent
    data class ChangePadding(val value: Int = 24) : SettingsIntent

    data class ChangeTheme(
        val value: ThemeValue = ThemeValue.SYSTEM,
    ) : SettingsIntent
}