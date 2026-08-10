package me.alexandervortex.shelfie.features.screens.viewer.mvi

sealed interface ViewerEffect {
    data class ShowToast(val message: String) : ViewerEffect
}
