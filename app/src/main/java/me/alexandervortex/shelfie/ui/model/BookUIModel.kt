package me.alexandervortex.shelfie.ui.model

import me.alexandervortex.shelfie.features.screens.addbook.PreviewScreenUIModel

data class BookUIModel(
    val id: String,
    val localPath: String,
    val titleInfo: PreviewScreenUIModel,
    val elements: List<ElementUIModel>,
    val progressIndex: Int = 0,
    val progressOffset: Int = 0
)