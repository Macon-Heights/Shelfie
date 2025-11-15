package me.alexandervortex.shelfie.features.viewer.mvi

import android.content.Context

sealed interface ViewerIntent {

    data class LoadBook(val id: String) : ViewerIntent
    data class BindService(val context: Context) : ViewerIntent
    data class UnbindService(val context: Context) : ViewerIntent

    data class TogglePlayPause(val index: Int) : ViewerIntent
    data object Next : ViewerIntent
    data object Prev : ViewerIntent
    data object ToggleTimer : ViewerIntent
    data object ToggleSpeed : ViewerIntent

    data object ToggleMenu : ViewerIntent
    data object ToggleSettings : ViewerIntent
}
