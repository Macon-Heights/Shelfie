package me.alexandervortex.shelfie.data.parser

enum class BookType(
    val extension: String
) {
    FB2("fb2"),
    EPUB("epub"),
    PDF("pdf"),
}
