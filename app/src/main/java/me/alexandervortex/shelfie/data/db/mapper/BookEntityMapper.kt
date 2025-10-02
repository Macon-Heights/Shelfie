package me.alexandervortex.shelfie.data.db.mapper

import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.ui.model.BookUI
import javax.inject.Inject

class BookEntityMapper
@Inject constructor() {

    fun toEntity(model: BookUI): BookEntity {
        val entity = BookEntity(
            id = model.titleInfo.id,
            localPath = model.titleInfo.localPath,
            title = model.titleInfo.title,
            author = model.titleInfo.author,
            year = model.titleInfo.date,
            scrollOffset = model.progressOffset,
            scrollIndex = model.progressIndex
        )
        return entity
    }
}
