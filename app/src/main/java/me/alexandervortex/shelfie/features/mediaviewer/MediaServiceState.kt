package me.alexandervortex.shelfie.features.mediaviewer

data class MediaServiceState(
    val isPlaying: Boolean = false,
    val error: String = "",

    // todo book
    val index: Int = 0,
    val part: Int = 0,
// todo   val buttonIconRes: Int = R.drawable.ic_play,
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