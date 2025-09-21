package me.alexandervortex.shelfie.data.model

import com.kursx.parser.fb2.Element
import com.kursx.parser.fb2.FictionBook
import com.kursx.parser.fb2.Section

data class BookModel(
    val list: List<String?>,
)

fun FictionBook?.toBookModel(): BookModel {
    val strings = this?.let { book ->
        book.body.sections.flatMap { section: Section ->
            section.elements.map { element: Element? ->
                element?.text
            }
        }
    }?.filterNotNull().orEmpty()
    return BookModel(strings)
}