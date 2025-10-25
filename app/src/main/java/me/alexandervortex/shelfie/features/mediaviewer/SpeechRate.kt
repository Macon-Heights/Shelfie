package me.alexandervortex.shelfie.features.mediaviewer

enum class SpeechRate(val speed: Float, val text: String) {

    DEFAULT(1f, " "),
    FAST(1.5f, "1.5x"),
    FASTEST(2f, "2x");

    fun getNext(): SpeechRate {
        val nextIndex = (ordinal + 1) % entries.size
        return entries[nextIndex]
    }
}