package me.alexandervortex.shelfie.features.mediaviewer

enum class TimerValue(val mins: Int, val text: String) {

    MIN_ZERO(0, ""),
    MIN_20(20, "20m"),
    MIN_40(40, "40m"),
    HOUR(60, "1h");

    fun getNext(): TimerValue {
        val nextIndex = (ordinal + 1) % entries.size
        return entries[nextIndex]
    }
}