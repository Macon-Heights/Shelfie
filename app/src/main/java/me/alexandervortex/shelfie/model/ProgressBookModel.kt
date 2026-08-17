package me.alexandervortex.shelfie.model

data class ProgressBookModel(
    val id: String,
    val localPath: String,
    val progress: ProgressModel,
    val book: ParsedBookModel
)