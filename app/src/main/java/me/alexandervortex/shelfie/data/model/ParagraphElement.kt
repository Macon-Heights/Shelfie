package me.alexandervortex.shelfie.data.model

data class ParagraphElement(
    val text: String,
) : ElementModel()

data class CiteElement(
    val text: String,
) : ElementModel()

data class PoemElement(
    val text: String,
) : ElementModel()

data class EmptyLineElement(
    val text: String,
) : ElementModel()

data class TableElement(
    val text: String,
) : ElementModel()

data class TextAuthorElement(
    val text: String,
) : ElementModel()

data class VElement(
    val text: String,
) : ElementModel()