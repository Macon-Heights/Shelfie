package me.alexandervortex.shelfie.features.settings.values

enum class ThemeValue(
    val value: Int,
) : Switchable<ThemeValue> {

    SYSTEM(0),
    LIGHT(1),
    DARK(2);

    companion object {

        fun fromValue(value: Int): ThemeValue {
            return entries.find { it.value == value } ?: SYSTEM
        }
    }
}