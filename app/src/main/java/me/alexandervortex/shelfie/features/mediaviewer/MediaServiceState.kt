package me.alexandervortex.shelfie.features.mediaviewer

data class MediaServiceState(
    val isPlaying: Boolean = false,
    val error: String = "",

    val speed: SpeechRate = SpeechRate.DEFAULT,
    val timer: TimerValue = TimerValue.OFF,

    val index: Int = 0,
    val part: Int = 0,

    val title: String? = null,
    val author: String? = null,
) {

    companion object {

        fun playingState(): MediaServiceState {
            return MediaServiceState(
                isPlaying = true,
            )
        }

        fun pausedState(): MediaServiceState {
            return MediaServiceState(
                isPlaying = false,
            )
        }
    }
}