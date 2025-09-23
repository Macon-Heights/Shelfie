package me.alexandervortex.shelfie.data.mapper

import android.net.Uri
import me.alexandervortex.shelfie.data.datasource.Parsed
import me.alexandervortex.shelfie.data.db.entiry.BookUri
import javax.inject.Inject

class BookEntityMapper
@Inject constructor() {

    fun fromParsed(book: Parsed, uri: Uri): BookUri {
        return BookUri(
            id = book.id,
            uri = uri.toString(),
            title = book.title,
            author = book.author,
            localPath = book.localPath
        )
    }
}
