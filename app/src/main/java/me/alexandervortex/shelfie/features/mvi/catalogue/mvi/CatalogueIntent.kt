package me.alexandervortex.shelfie.features.mvi.catalogue.mvi

import android.net.Uri

sealed interface CatalogueIntent {
    data class ImportBook(val uri: Uri) : CatalogueIntent
    data class ToggleRemoveMode(val id: String) : CatalogueIntent
    data class ToggleBookCheck(val id: String) : CatalogueIntent
    data class TogglePopup(val isEnabled: Boolean) : CatalogueIntent
    data object RemoveChecked : CatalogueIntent
}