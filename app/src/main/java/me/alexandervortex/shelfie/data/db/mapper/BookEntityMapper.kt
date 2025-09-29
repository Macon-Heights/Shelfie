package me.alexandervortex.shelfie.data.db.mapper

import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.ui.model.BookUi
import javax.inject.Inject

class BookEntityMapper
@Inject constructor() {

    fun toEntity(model: BookUi): BookEntity {
        return BookEntity(
            id = model.id,
            localPath = model.localPath,
            title = model.title,
            author = model.author,
            year = model.year,
        )
    }
}
