package me.alexandervortex.shelfie.features.settings

sealed interface SettingsIntent {
    data class ChangeFont(val value: Int) : SettingsIntent
    data class ChangeTtsSpeed(val value: Float) : SettingsIntent
    data class ChangeStoppingTime(val value: Long) : SettingsIntent
}