package me.alexandervortex.shelfie.features.settings

sealed class SettingsIntent {
    class ChangeFont(val value: Int) : SettingsIntent()
}
