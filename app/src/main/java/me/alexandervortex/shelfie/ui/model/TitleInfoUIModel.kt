package me.alexandervortex.shelfie.ui.model

import me.alexandervortex.shelfie.ui.component.new.ImageUIModel

data class TitleInfoUIModel(
    val title: String?,
    val date: String?,
    val author: String?,
    val annotation: String?,
    val genre: String?,
    val lang: String?,
    val coverImage: ImageUIModel?,
    val manyImages: List<ImageUIModel?>
)