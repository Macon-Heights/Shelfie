package me.alexandervortex.shelfie.ui.model

import me.alexandervortex.shelfie.model.PreviewBookModel
import me.alexandervortex.shelfie.ui.model.new.ElementUIModel

data class BookUIModel(
    val id: String,
    val localPath: String,
    val titleInfo: PreviewBookModel,
    val elements: List<ElementUIModel>,
    val progressIndex: Int = 0,
    val progressOffset: Int = 0
)