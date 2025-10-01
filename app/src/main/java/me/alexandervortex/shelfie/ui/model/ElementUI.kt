package me.alexandervortex.shelfie.ui.model

sealed interface ElementUI {
    data class Paragraph(val text: String) : ElementUI
    object EmptyLine : ElementUI
    data class Image(val data: ByteArray?) : ElementUI
    data class Container(
        val type: String,
        val children: List<ElementUI>
    ) : ElementUI
}