package me.alexandervortex.shelfie.features.mediaviewer

data class MediaServiceState(
    val isPlaying: Boolean = false,
    val error: String = "",

    val speed: Float = 1f,
    val timer: Int = 0,

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