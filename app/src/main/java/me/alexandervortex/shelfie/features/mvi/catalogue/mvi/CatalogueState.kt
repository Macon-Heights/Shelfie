package me.alexandervortex.shelfie.features.mvi.catalogue.mvi

import me.alexandervortex.shelfie.ui.model.BookComponentSkeleton
import me.alexandervortex.shelfie.ui.model.Bookable

data class CatalogueState(
    val isPopup: Boolean = false,
    val isRemoveMode: Boolean = false,
    val books: List<Bookable> = listOf(
        BookComponentSkeleton,
        BookComponentSkeleton,
        BookComponentSkeleton,
        BookComponentSkeleton,
        BookComponentSkeleton,
        BookComponentSkeleton,
        BookComponentSkeleton,
        BookComponentSkeleton,
        BookComponentSkeleton,
        BookComponentSkeleton,
        BookComponentSkeleton,
        BookComponentSkeleton,
    ),
    val error: String? = null,
)