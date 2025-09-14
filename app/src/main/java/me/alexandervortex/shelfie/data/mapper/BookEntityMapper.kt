package me.alexandervortex.shelfie.data.mapper

import android.net.Uri
import com.kursx.parser.fb2.FictionBook
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import javax.inject.Inject

class BookEntityMapper
@Inject constructor() {

    fun map(book: FictionBook?, uri: Uri): BookEntity? {
        return book?.let { fb2book ->
            BookEntity(
                id = fb2book.description.hashCode(),
                fb2DocumentId = fb2book.description?.documentInfo?.id,
                title = fb2book.title,
                uri = uri.toString()
            )
        }
    }
}
