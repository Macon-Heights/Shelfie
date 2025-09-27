package me.alexandervortex.shelfie.data.db.mapper

import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.data.model.BookModel
import javax.inject.Inject

class BookEntityMapper
@Inject constructor() {

    fun toEntity(model: BookModel): BookEntity {
        return BookEntity(
            id = model.id,
            localPath = model.localPath,
            title = model.title,
            author = model.author,
            year = model.year,
        )
    }
}
