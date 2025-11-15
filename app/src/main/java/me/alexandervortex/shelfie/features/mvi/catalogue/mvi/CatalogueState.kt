package me.alexandervortex.shelfie.features.mvi.catalogue.mvi

import me.alexandervortex.shelfie.ui.model.CatalogueItemUI

data class CatalogueState(
    val isPopup: Boolean = false,
    val isRemoveMode: Boolean = false,
    val books: List<CatalogueItemUI> = emptyList(),
    val error: String? = null,
)