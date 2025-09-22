package me.alexandervortex.shelfie.data.mapper

import android.net.Uri
import com.kursx.parser.fb2.FictionBook
import me.alexandervortex.shelfie.data.db.entiry.BookUri
import javax.inject.Inject

class BookEntityMapper
@Inject constructor() {

    fun map(book: FictionBook?, uri: Uri): BookUri? {
        return book?.let { fb2book ->
            BookUri(
                id = fb2book.description.hashCode(),
                fb2DocumentId = fb2book.description?.documentInfo?.id,
                uri = uri.toString(),
                title = fb2book.title,
                author = fb2book.authors.firstOrNull()?.fullName,
                image = fb2book.binaries.values.firstOrNull {
                    it.contentType.contains("image")
                }?.binary?.toByteArray()
            )
        }
    }
}
