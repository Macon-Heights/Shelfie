package me.alexandervortex.shelfie.features.catalogue.model

import me.alexandervortex.shelfie.data.db.entiry.BookEntity

data class CatalogueUiState(val bookEntities: List<BookEntity>)