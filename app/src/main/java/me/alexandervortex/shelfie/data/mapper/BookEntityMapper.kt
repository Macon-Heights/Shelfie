package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.data.db.entity.BookEntity
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
            scrollIndex = model.progressIndex,
            scrollOffset = model.progressOffset,
            elements = model.elements.size
        )
        return entity
    }
}