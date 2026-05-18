package me.alexandervortex.shelfie.features.mvi.viewer.mvi

import me.alexandervortex.shelfie.features.player.MediaServiceState
import me.alexandervortex.shelfie.ui.model.BookUIModel

data class ViewerState(
    val book: BookUIModel? = null,
    val serviceState: MediaServiceState = MediaServiceState(),
    val isMenuVisible: Boolean = true,
    val isSettingsVisible: Boolean = false,
    val error: String = "",
)