package me.alexandervortex.shelfie.data.mapper

import com.kursx.parser.fb2.FictionBook
import me.alexandervortex.shelfie.data.model.BookFile
import me.alexandervortex.shelfie.data.model.BookModel
import javax.inject.Inject

@Deprecated("GAVNO")
class FictionBookParser
@Inject constructor(
    private val sectionMapper: SectionModelMapper,
) {

    fun parse(
        file: BookFile,
        id: String,
    ): BookModel {
        val fictionBook = FictionBook(file.file)
        val localPath = file.path

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