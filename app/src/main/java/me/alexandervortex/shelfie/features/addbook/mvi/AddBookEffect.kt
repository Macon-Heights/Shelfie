package me.alexandervortex.shelfie.features.addbook.mvi

sealed interface AddBookEffect {
    data class ShowToast(val message: String) : AddBookEffect
    data class NavigateTo(val route: String) : AddBookEffect
}