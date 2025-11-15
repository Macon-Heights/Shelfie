package me.alexandervortex.shelfie.base.ext

import me.alexandervortex.shelfie.data.db.entity.BookEntity
import me.alexandervortex.shelfie.ui.model.CatalogueItemUI

fun BookEntity.toBookComponentModel(): CatalogueItemUI.Model {
    return CatalogueItemUI.Model(
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