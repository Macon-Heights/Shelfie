package me.alexandervortex.shelfie.features.catalogue.ui.model

import me.alexandervortex.shelfie.data.db.entiry.BookEntity

sealed interface UIState {

    data object CatalogueLoadingState : UIState

    data class CatalogueBooksState(
        val books: List<BookEntity>,
    ) : UIState
}