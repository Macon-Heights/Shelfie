package me.alexandervortex.shelfie.features.mvi.addbook.mvi

sealed interface AddBookEffect {
    data class ShowToast(val message: String) : AddBookEffect
    data class NavigateTo(val route: String) : AddBookEffect
    data object Close : AddBookEffect
}