package me.alexandervortex.shelfie.features.mediaviewer

enum class TimerValue(
    val value: Int,
    val text: String,
) : Switchable<TimerValue> {

    OFF(0, " "),
    TEST(1, "1m"),
    MIN_20(20, "20m"),
    MIN_40(40, "40m"),
    HOUR(60, "1h");

    override fun title() = text
}

interface Switchable<T : Enum<T>> {

    fun title(): String
}

inline fun <reified T> T.next(): T where T : Enum<T>, T : Switchable<T> {
    val values = enumValues<T>()
    val nextIndex = (ordinal + 1) % values.size
    return values[nextIndex]
}

inline fun <reified T> T.prev(): T where T : Enum<T>, T : Switchable<T> {
    val values = enumValues<T>()
    val prevIndex = if (ordinal - 1 < 0) values.lastIndex else ordinal - 1
    return values[prevIndex]
}