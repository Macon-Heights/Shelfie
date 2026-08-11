package me.alexandervortex.shelfie.feature.viewer.mvi

sealed interface ViewerEffect {
    data class ShowToast(val message: String) : ViewerEffect
}
