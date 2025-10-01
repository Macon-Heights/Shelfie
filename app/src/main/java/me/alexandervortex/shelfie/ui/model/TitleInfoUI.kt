package me.alexandervortex.shelfie.ui.model

data class TitleInfoUI(
    val id: String,
    val localPath: String,

    val title: String?,
    val year: String?,
    val author: String?,
    val annotation: String?,
    val genre: String?,
    val coverImage: ByteArray?,
)