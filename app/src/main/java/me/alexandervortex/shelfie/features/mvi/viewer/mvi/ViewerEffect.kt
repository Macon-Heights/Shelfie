package me.alexandervortex.shelfie.features.mvi.viewer.mvi

sealed interface ViewerEffect {
    data class ShowToast(val message: String) : ViewerEffect
}
