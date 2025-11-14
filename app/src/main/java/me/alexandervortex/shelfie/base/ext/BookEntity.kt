package me.alexandervortex.shelfie.base.ext

import me.alexandervortex.shelfie.data.db.entity.BookEntity
import me.alexandervortex.shelfie.ui.model.Bookable

fun BookEntity.toBookComponentModel(): Bookable.BookComponentModel {
    return Bookable.BookComponentModel(
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