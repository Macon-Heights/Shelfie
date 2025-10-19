package me.alexandervortex.shelfie.features.catalogue.ui.model

import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.features.catalogue.ui.preview.CataloguePreviewData.getBooks

sealed interface UIState {

    data object CatalogueLoadingState : UIState

    data class CatalogueBooksState(
        val books: List<BookEntity> = getBooks(),
    ) : UIState
}