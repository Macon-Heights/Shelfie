package me.alexandervortex.shelfie.features.catalogue.ui.model

import me.alexandervortex.shelfie.data.db.entiry.BookEntity

data class CatalogueUiState(val books: List<BookEntity>)