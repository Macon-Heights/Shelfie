package me.alexandervortex.shelfie.base.ext

import me.alexandervortex.shelfie.model.BookDocument
import me.alexandervortex.shelfie.model.BookNode

fun BookDocument.toSpineSection(
    id: String,
): BookNode.Section {

    val firstHeading = children
        .firstOrNull() as? BookNode.Heading

    return BookNode.Section(
        id = "epub:$id",
        title = firstHeading?.content,
        children = if (firstHeading != null) {
            children.drop(1)
        } else {
            children
        },
    )
}