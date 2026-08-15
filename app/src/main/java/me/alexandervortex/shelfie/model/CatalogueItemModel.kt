package me.alexandervortex.shelfie.model

data class CatalogueItemModel(
    val id: String,
    val localPath: String,
    val title: String?,
    val author: String?,
    val year: String?,
    val scrollIndex: Int = 0,
    val scrollOffset: Int = 0,
    val elements: Int = 0,
)