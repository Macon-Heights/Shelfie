package me.alexandervortex.shelfie.feature.catalogue.mvi

import me.alexandervortex.shelfie.feature.updater.AppUpdate

sealed interface CatalogueEffect {
    data class ShowToast(val message: String) : CatalogueEffect
    data class NavigateTo(val route: String) : CatalogueEffect
    data class ShowUpdateDialog(val update: AppUpdate) : CatalogueEffect
}