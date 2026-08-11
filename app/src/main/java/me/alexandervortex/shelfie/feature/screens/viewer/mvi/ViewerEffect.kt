package me.alexandervortex.shelfie.feature.screens.viewer.mvi

sealed interface ViewerEffect {
    data class ShowToast(val message: String) : ViewerEffect
}
