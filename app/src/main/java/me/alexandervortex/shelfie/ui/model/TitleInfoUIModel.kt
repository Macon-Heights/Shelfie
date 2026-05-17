package me.alexandervortex.shelfie.ui.model

data class TitleInfoUIModel(
    val title: String?,
    val date: String?,
    val author: String?,
    val annotation: String?,
    val genre: String?,
    val lang: String?,
    val coverImage: ByteArray?,
)