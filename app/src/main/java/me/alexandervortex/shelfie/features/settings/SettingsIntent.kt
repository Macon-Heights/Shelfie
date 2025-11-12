package me.alexandervortex.shelfie.features.settings

sealed interface SettingsIntent {
    data class ChangeFont(val value: Int = 16) : SettingsIntent
    data class ChangeTtsSpeed(val value: Float) : SettingsIntent
    data class ChangeStoppingTime(val value: Long) : SettingsIntent
}