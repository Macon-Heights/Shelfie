package me.alexandervortex.shelfie.data.mapper

import com.kursx.parser.fb2.FictionBook
import com.kursx.parser.fb2.Section
import me.alexandervortex.shelfie.data.model.BookModel
import me.alexandervortex.shelfie.data.model.SectionModel
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
        val sections = fictionBook.body.sections

        return BookModel(
            id = id,
            localPath = localPath,
            title = title,
            author = author,
            year = year,
            sections = sections.map { section ->
                mapSection(section)
            },
        )
    }

    private fun mapSection(section: Section): SectionModel {
        return SectionModel(
            section.elements.map {
                "${it.text.replace("\n", " ")}\n\n\n"
            }
        )
    }
}