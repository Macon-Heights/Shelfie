package me.alexandervortex.shelfie.feature.player

import me.alexandervortex.shelfie.feature.settings.values.SpeechRateValue
import me.alexandervortex.shelfie.feature.settings.values.TimerValue

data class MediaServiceState(
    val isPlaying: Boolean = false,
    val error: String = "",

    val speed: SpeechRateValue = SpeechRateValue.DEFAULT,
    val timer: TimerValue = TimerValue.OFF,

    val index: Int = 0,
    val offset: Int = 0,

    val title: String? = null,
    val author: String? = null,
)