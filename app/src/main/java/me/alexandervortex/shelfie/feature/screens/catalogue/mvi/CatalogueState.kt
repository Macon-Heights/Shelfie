package me.alexandervortex.shelfie.feature.screens.catalogue.mvi

import me.alexandervortex.shelfie.ui.model.CatalogueItemUIModel

data class CatalogueState(
    val isPopup: Boolean = false,
    val isRemoveMode: Boolean = false,
    val books: List<CatalogueItemUIModel> = emptyList(),
    val error: String? = null,
)