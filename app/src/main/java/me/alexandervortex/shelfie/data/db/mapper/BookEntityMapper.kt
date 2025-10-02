package me.alexandervortex.shelfie.data.db.mapper

import me.alexandervortex.shelfie.base.Lg
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.ui.model.BookUI
import javax.inject.Inject

class BookEntityMapper
@Inject constructor() {

    private val lg = Lg("BookEntityMapper")
    fun toEntity(model: BookUI): BookEntity {
        lg.log("toEntity start")
        val entity = BookEntity(
            id = model.titleInfo.id,
            localPath = model.titleInfo.localPath,
            title = model.titleInfo.title,
            author = model.titleInfo.author,
            year = model.titleInfo.date,
        )
        lg.log("toEntity end")
        return entity
    }
}
