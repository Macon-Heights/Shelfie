package me.alexandervortex.shelfie.data.model

import com.kursx.parser.fb2.Element
import com.kursx.parser.fb2.FictionBook
import com.kursx.parser.fb2.Section

data class FB2Model(
    val list: List<String?>,
)

fun FictionBook?.toFB2Model(): FB2Model {
    val strings = this?.let { book ->
        book.body.sections.flatMap { section: Section ->
            section.elements.map { element: Element? ->
                element?.text
            }
        }
    }?.filterNotNull().orEmpty()
    return FB2Model(strings)
}