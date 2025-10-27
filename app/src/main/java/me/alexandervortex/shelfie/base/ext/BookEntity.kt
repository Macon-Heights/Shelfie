package me.alexandervortex.shelfie.base.ext

import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.ui.component.BookComponentModel

fun BookEntity.toBookComponentModel(): BookComponentModel {
    return BookComponentModel(
        isChecked = null,
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