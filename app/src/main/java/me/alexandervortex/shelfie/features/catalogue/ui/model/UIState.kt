package me.alexandervortex.shelfie.features.catalogue.ui.model

import me.alexandervortex.shelfie.features.catalogue.ui.preview.CataloguePreviewData.getBooks
import me.alexandervortex.shelfie.ui.component.BookComponentModel

sealed interface UIState {

    data object CatalogueLoadingState : UIState

    data class CatalogueBooksState(
        val books: List<BookComponentModel> = getBooks(),
    ) : UIState
}