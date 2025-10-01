package me.alexandervortex.shelfie.ui.model

sealed interface ElementUI {
    data class TextUI(val text: String) : ElementUI
    data class ImageUI(val image: ByteArray?) : ElementUI
    data class EmptyLineUI(val dummy: Boolean = true) : ElementUI
    data class StackElementUI(
        val type: String,
        val children: List<ElementUI>
    ) : ElementUI
}