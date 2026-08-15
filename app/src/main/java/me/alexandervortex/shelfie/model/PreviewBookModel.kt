package me.alexandervortex.shelfie.model

data class PreviewBookModel(
    val title: String? = null,
    val date: String? = null,
    val author: String? = null,
    val annotation: String? = null,
    val genre: String? = null,
    val lang: String? = null,
    val coverImage: ImageModel? = null,
    val gallery: List<ImageModel?>? = null
)