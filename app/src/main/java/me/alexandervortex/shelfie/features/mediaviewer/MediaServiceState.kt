package me.alexandervortex.shelfie.features.mediaviewer

import androidx.annotation.DrawableRes
import me.alexandervortex.shelfie.R
import me.alexandervortex.shelfie.features.mediaplayer.ServiceState

data class MediaServiceState(
    val isPlaying: Boolean = false,
    val error: String = "",
    val index: Int = 0,
    val part: Int = 0,
    @DrawableRes
    val buttonIconRes: Int = R.drawable.ic_play,
) {

    companion object {

        fun playingState(): ServiceState {
            return ServiceState(
                isPlaying = true,
                error = "",
                index = 0,
                part = 0
            )
        }

        fun pausedState(): ServiceState {
            return ServiceState(
                isPlaying = false,
                error = "",
                index = 0,
                part = 0
            )
        }

        fun nullState(): ServiceState {
            return ServiceState(
                isPlaying = true,
                error = "",
                index = 0,
                part = 0
            )
        }

        fun errorState(): ServiceState {
            return ServiceState(
                isPlaying = true,
                error = "",
                index = 0,
                part = 0
            )
        }
    }
}