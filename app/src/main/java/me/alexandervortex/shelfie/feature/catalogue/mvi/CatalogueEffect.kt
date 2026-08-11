package me.alexandervortex.shelfie.feature.catalogue.mvi

sealed interface CatalogueEffect {
    data class ShowToast(val message: String) : CatalogueEffect
    data class NavigateTo(val route: String) : CatalogueEffect
}