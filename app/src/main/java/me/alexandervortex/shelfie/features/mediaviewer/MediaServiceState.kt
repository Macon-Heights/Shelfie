package me.alexandervortex.shelfie.features.mediaviewer

import me.alexandervortex.shelfie.features.settings.values.SpeechRateValue
import me.alexandervortex.shelfie.features.settings.values.TimerValue

data class MediaServiceState(
    val isPlaying: Boolean = false,
    val error: String = "",

    val speed: SpeechRateValue = SpeechRateValue.DEFAULT,
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