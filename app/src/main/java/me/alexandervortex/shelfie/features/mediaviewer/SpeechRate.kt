package me.alexandervortex.shelfie.features.mediaviewer

enum class SpeechRate(val speed: Float) {

    DEFAULT(1f),
    FAST(1.5f),
    FASTEST(2f);

    fun getNext(): SpeechRate {
        val nextIndex = (ordinal + 1) % entries.size
        return entries[nextIndex]
    }
}