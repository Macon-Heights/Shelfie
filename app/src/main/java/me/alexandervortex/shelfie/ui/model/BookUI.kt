package me.alexandervortex.shelfie.ui.model

data class BookUI(
    val titleInfo: TitleInfoUI,
    val elements: List<ElementUI>,
    val progressIndex: Int = 0,
    val progressOffset: Int = 0
)