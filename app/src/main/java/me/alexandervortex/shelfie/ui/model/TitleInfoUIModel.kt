package me.alexandervortex.shelfie.ui.model

data class TitleInfoUIModel(
    val id: String,
    val localPath: String,

    val title: String?,
    val date: String?,
    val author: String?,
    val annotation: String?,
    val genre: String?,
    val lang: String?,
    val coverImage: ByteArray?,
)