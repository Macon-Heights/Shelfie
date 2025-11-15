package me.alexandervortex.shelfie.base.ext

import me.alexandervortex.shelfie.data.db.entity.BookEntity
import me.alexandervortex.shelfie.ui.model.CatalogueItemUIModel

fun BookEntity.toBookComponentModel(): CatalogueItemUIModel.Model {
    return CatalogueItemUIModel.Model(
        isChecked = false,
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