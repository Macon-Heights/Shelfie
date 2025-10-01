package me.alexandervortex.shelfie.ui.model

sealed interface ElementUI {

    object Title : ElementUI
    object Epigraph : ElementUI
    data class Image(val data: ByteArray?) : ElementUI
    object Annotation : ElementUI
    data class Paragraph(
        val text: String?,
    ) : ElementUI

    object Poem : ElementUI
    object Cite : ElementUI
    object Table : ElementUI
    object Subtitle : ElementUI
    object EmptyLine : ElementUI
    object Section : ElementUI
}