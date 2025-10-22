package me.alexandervortex.shelfie.features.mediaviewer

import androidx.annotation.DrawableRes
import me.alexandervortex.shelfie.R

data class MediaServiceState(
    val isPlaying: Boolean = false,
    val error: String = "",
    val index: Int = 0,
    val part: Int = 0,
    @DrawableRes
    val buttonIconRes: Int = R.drawable.ic_play,
) {

    companion object {

        fun playingState(): MediaServiceState {
            return MediaServiceState(
                isPlaying = true,
                error = "",
                index = 0,
                part = 0
            )
        }

        fun pausedState(): MediaServiceState {
            return MediaServiceState(
                isPlaying = false,
                error = "",
                index = 0,
                part = 0
            )
        }

        fun nullState(): MediaServiceState {
            return MediaServiceState(
                isPlaying = true,
                error = "",
                index = 0,
                part = 0
            )
        }

        fun errorState(): MediaServiceState {
            return MediaServiceState(
                isPlaying = true,
                error = "",
                index = 0,
                part = 0
            )
        }
    }
}