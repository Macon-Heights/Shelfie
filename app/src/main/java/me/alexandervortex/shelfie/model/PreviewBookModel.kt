package me.alexandervortex.shelfie.model

import me.alexandervortex.shelfie.ui.component.new.ImageUIModel

data class PreviewBookModel(
    val title: String? = null,
    val date: String? = null,
    val author: String? = null,
    val annotation: String? = null,
    val genre: String? = null,
    val lang: String? = null,
    val coverImage: ImageUIModel? = null,
    val gallery: List<ImageUIModel?>? = null
)