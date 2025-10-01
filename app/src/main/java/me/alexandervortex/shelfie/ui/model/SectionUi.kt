package me.alexandervortex.shelfie.ui.model

// Глава или подраздел
data class SectionUi(
    val title: String?,
    val blocks: List<BlockUi>,
//    val subsections: List<SectionUi> // Для поддержки иерархии
)