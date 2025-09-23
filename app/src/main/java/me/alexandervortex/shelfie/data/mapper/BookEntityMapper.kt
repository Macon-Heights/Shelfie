package me.alexandervortex.shelfie.data.mapper

import android.net.Uri
import me.alexandervortex.shelfie.data.datasource.Parsed
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import javax.inject.Inject

class BookEntityMapper
@Inject constructor() {

    fun toEntity(book: Parsed, uri: Uri): BookEntity {
        return BookEntity(
            id = book.id,
            uri = uri.toString(),
            title = book.title,
            author = book.author,
            localPath = book.localPath
        )
    }
}
