package me.alexandervortex.shelfie.ui.model

import me.alexandervortex.shelfie.model.ImageModel

sealed interface UI {

    data object EmptyLine : UI

    data class Image(
        val image: ImageModel,
    ) : UI

    data class ComplexText(
        val parts: List<StyledText>,
    ) : UI

    data object Skeleton : UI

    data class Heading(
        val level: Int,
        val content: ComplexText,
    ) : UI
}