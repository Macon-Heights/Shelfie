package me.alexandervortex.shelfie.features.settings.values

import me.alexandervortex.shelfie.R

enum class ThemeValue(
    val value: Int,
    val textResId: Int,
) : Switchable<ThemeValue> {

    SYSTEM(0, R.string.settings_theme_system),
    DARK(1, R.string.settings_theme_dark),
    LIGHT(2, R.string.settings_theme_light);

    companion object {

        fun fromValue(value: Int): ThemeValue {
            return entries.find { it.value == value } ?: SYSTEM
        }
    }
}