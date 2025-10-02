package me.alexandervortex.shelfie.ui.model

sealed interface ElementUI {

    data class TextUI(
        val text: String,
    ) : ElementUI

    data class ImageUI(
        val image: ByteArray,
    ) : ElementUI

    data object EmptyLine : ElementUI
}