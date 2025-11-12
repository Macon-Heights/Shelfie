package me.alexandervortex.shelfie.features.settings.values

enum class ThemeValue(
    val value: Int,
    val text: String,
) : Switchable<ThemeValue> {

    OFF(0, " "),
    TEST(1, "1m"),
    MIN_20(20, "20m"),
    MIN_40(40, "40m"),
    HOUR(60, "1h");
}