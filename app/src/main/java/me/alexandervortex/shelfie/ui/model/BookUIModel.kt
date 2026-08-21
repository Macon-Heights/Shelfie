package me.alexandervortex.shelfie.ui.model

import me.alexandervortex.shelfie.model.PreviewBookModel

data class BookUIModel(
    val id: String,
    val localPath: String,
    val titleInfo: PreviewBookModel,
    val elements: List<UI>,
    val progressIndex: Int = 0,
    val progressOffset: Int = 0
)