package me.alexandervortex.shelfie.features.mediaviewer

enum class TimerValue(val mins: Int, val text: String) {

    OFF(0, ""),
    TEST(2, "2m"),
    MIN_20(20, "20m"),
    MIN_40(40, "40m"),
    HOUR(60, "1h");

    fun getNext(): TimerValue {
        val nextIndex = (ordinal + 1) % entries.size
        return entries[nextIndex]
    }
}