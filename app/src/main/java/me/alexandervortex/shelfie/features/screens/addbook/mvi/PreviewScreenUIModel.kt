package me.alexandervortex.shelfie.features.screens.addbook.mvi

import me.alexandervortex.shelfie.ui.component.new.ImageUIModel

data class PreviewScreenUIModel(
    val title: String? = null,
    val date: String? = null,
    val author: String? = null,
    val annotation: String? = null,
    val genre: String? = null,
    val lang: String? = null,
    val coverImage: ImageUIModel? = null,
    val manyImages: List<ImageUIModel?>? = null
)