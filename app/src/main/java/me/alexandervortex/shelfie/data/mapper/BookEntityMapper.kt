package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.data.db.BookEntity
import me.alexandervortex.shelfie.model.CatalogueItemModel
import me.alexandervortex.shelfie.ui.model.BookUIModel
import javax.inject.Inject

class BookEntityMapper
@Inject constructor() {

    fun toEntity(model: BookUIModel): BookEntity {
        val entity = BookEntity(
            id = model.id,
            localPath = model.localPath,
            title = model.titleInfo.title,
            author = model.titleInfo.author,
            year = model.titleInfo.date,
            scrollIndex = model.progressIndex,
            scrollOffset = model.progressOffset,
            elements = model.elements.size
        )
        return entity
    }

    fun toModel(entity: BookEntity): CatalogueItemModel {
        return with(entity) {
            CatalogueItemModel(
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
    }
}