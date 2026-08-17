package me.alexandervortex.shelfie.model

data class ParsedBookModel(
    val titleInfo: PreviewBookModel,
    val document: BookDocument,
)