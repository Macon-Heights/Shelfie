package me.alexandervortex.shelfie.features.mvi.catalogue.mvi

import me.alexandervortex.shelfie.ui.component.BookComponentModel

data class CatalogueState(
    val isLoading: Boolean = false,
    val isRemoveMode: Boolean = false,
    val books: List<BookComponentModel> = emptyList(),
    val error: String? = null,
)