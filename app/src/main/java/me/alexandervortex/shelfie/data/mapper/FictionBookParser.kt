package me.alexandervortex.shelfie.data.mapper

import com.kursx.parser.fb2.FictionBook
import me.alexandervortex.shelfie.data.model.BookModel
import java.io.File
import javax.inject.Inject

@Deprecated("GAVNO")
class FictionBookParser
@Inject constructor(
    private val sectionMapper: SectionModelMapper,
) {

    fun parse(
        file: File,
        id: String,
    ): BookModel {
        val fictionBook = FictionBook(file)
        val localPath = file.absolutePath

        val title = fictionBook.description.titleInfo.bookTitle
        val author = fictionBook.description.titleInfo.authors.firstOrNull()?.fullName
        val year = fictionBook.description.publishInfo.year
        val sections = fictionBook.body.sections

        return BookModel(
            id = id,
            localPath = localPath,
            title = title,
            author = author,
            year = year,
            sections = sections.map { section ->
                sectionMapper.map(section)
            },
        )
    }
}