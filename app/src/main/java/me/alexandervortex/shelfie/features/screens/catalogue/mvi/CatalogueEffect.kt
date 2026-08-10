package me.alexandervortex.shelfie.features.screens.catalogue.mvi

sealed interface CatalogueEffect {
    data class ShowToast(val message: String) : CatalogueEffect
    data class NavigateTo(val route: String) : CatalogueEffect
}