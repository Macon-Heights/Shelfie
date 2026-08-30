package me.alexandervortex.shelfie.feature.viewer.mvi

import me.alexandervortex.shelfie.feature.player.MediaServiceState
import me.alexandervortex.shelfie.ui.model.BookUIModel

data class ViewerState(
    val book: BookUIModel? = null,
    val serviceState: MediaServiceState = MediaServiceState(),
    val isMenuVisible: Boolean = true,
    val isSectionsVisible: Boolean = false,
    val isSettingsVisible: Boolean = false,
    val error: String = "",
)