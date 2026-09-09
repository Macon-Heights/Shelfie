package me.alexandervortex.shelfie.feature.viewer.mvi

sealed interface ViewerIntent {

    data class LoadBook(val id: String) : ViewerIntent

    data object BindService : ViewerIntent

    data object UnbindService : ViewerIntent
    data class SaveScrollStateOnDispose(
        val id: String,
        val index: Int,
        val offset: Int,
    ) : ViewerIntent

    data class TogglePlayPause(val index: Int) : ViewerIntent
    data object Next : ViewerIntent
    data object Sections : ViewerIntent
    data object ToggleTimer : ViewerIntent
    data object ToggleSpeed : ViewerIntent

    data object ToggleMenu : ViewerIntent
    data object ToggleSettings : ViewerIntent
    data object ToggleSections : ViewerIntent
}
