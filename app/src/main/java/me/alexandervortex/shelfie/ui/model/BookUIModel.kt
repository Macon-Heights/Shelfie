package me.alexandervortex.shelfie.ui.model

data class BookUIModel(
    val id: String,
    val localPath: String,
    val titleInfo: TitleInfoUIModel,
    val elements: List<ElementUIModel>,
    val progressIndex: Int = 0,
    val progressOffset: Int = 0
)