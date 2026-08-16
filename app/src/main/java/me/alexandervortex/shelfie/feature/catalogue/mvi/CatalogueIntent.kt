package me.alexandervortex.shelfie.feature.catalogue.mvi

sealed interface CatalogueIntent {
    data class ToggleRemoveMode(val id: String? = null) : CatalogueIntent
    data class ToggleBookCheck(val id: String) : CatalogueIntent
    data class TogglePopup(val isEnabled: Boolean) : CatalogueIntent
    data object RemoveChecked : CatalogueIntent
}