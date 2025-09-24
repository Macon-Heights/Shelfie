package me.alexandervortex.shelfie.data.mapper

import com.kursx.parser.fb2.FictionBook
import me.alexandervortex.shelfie.data.model.BookModel
import java.io.File
import javax.inject.Inject

class BookModelMapper
@Inject constructor() {

    fun map(
        file: File,
        id: String,
    ): BookModel {
        val fictionBook = FictionBook(file)
        val localPath = file.absolutePath
        val title = fictionBook.description.titleInfo.bookTitle
        val author = fictionBook.description.titleInfo.authors.firstOrNull()?.fullName
        val year = fictionBook.description.publishInfo.year

        return BookModel(
            id = id,
            localPath = localPath,
            title = title,
            author = author,
            year = year,
            fb2 = fictionBook,
        )
    }
}