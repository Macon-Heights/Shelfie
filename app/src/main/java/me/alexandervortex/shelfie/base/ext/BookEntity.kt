package me.alexandervortex.shelfie.base.ext

import me.alexandervortex.shelfie.data.db.BookEntity
import me.alexandervortex.shelfie.model.CatalogueItemModel
import me.alexandervortex.shelfie.ui.model.CatalogueItemUIModel

fun BookEntity.toModel(): CatalogueItemModel {
    return CatalogueItemModel(
        id = id,
        localPath = localPath,
        title = title,
        author = author,
        year = year,
        scrollIndex = scrollIndex,
        scrollOffset = scrollOffset,
        elements = elements
    )
}