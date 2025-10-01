package me.alexandervortex.shelfie.ui.model

// Книга целиком
data class BookUI(
    val titleInfo: TitleInfoUI,
    val sections: List<SectionUi>,
)