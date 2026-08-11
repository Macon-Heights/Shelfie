package me.alexandervortex.shelfie.features.screens.preview

sealed interface PreviewScreenEffect {
    data class ShowToast(val message: String) : PreviewScreenEffect
    data class NavigateTo(val route: String) : PreviewScreenEffect
    data object Close : PreviewScreenEffect
}