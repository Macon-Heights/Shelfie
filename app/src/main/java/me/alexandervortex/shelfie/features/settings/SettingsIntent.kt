package me.alexandervortex.shelfie.features.settings

sealed interface SettingsIntent {
    data class ChangeFont(val value: Int = 16) : SettingsIntent
    data class ChangeLineHeight(val value: Float = 1f) : SettingsIntent
    data class ChangePadding(val value: Int = 24) : SettingsIntent
}