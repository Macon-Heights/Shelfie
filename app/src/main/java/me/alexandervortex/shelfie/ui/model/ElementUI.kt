package me.alexandervortex.shelfie.ui.model

sealed interface ElementUI {

    data class TextUI(
        val words: List<String>,
    ) : ElementUI

    data class ImageUI(
        val image: ByteArray,
    ) : ElementUI

    data object EmptyLine : ElementUI
}