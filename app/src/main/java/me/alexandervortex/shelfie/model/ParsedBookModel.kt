package me.alexandervortex.shelfie.model

import me.alexandervortex.shelfie.ui.model.ElementUIModel

data class ParsedBookModel(
    val titleInfo: PreviewBookModel,
    val elements: List<ElementUIModel>,
)