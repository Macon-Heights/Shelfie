package me.alexandervortex.shelfie.features.viewer.mvi

sealed interface ViewerEffect {
    data class ShowToast(val message: String) : ViewerEffect
}
