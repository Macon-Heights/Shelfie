package me.alexandervortex.shelfie.features.mediaplayer

import androidx.annotation.DrawableRes
import me.alexandervortex.shelfie.R

data class ServiceState(
    val isPlaying: Boolean = false,
    val error: String = "",
    val index: Int = 0,
    val part: Int = 0,
    @DrawableRes val buttonIconRes: Int = R.drawable.ic_play,
)