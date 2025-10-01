package me.alexandervortex.shelfie.ui.model

@Deprecated("LEGACY")
sealed interface LegacyElementUI {

    data class Section(
        val elements: List<LegacyElementUI?>,
    ) : LegacyElementUI

    object Title : LegacyElementUI
    object Epigraph : LegacyElementUI
    data class Image(val data: ByteArray?) : LegacyElementUI
    object Annotation : LegacyElementUI
    data class Paragraph(
        val text: String?,
    ) : LegacyElementUI

    object Poem : LegacyElementUI
    object Cite : LegacyElementUI
    object Table : LegacyElementUI
    object Subtitle : LegacyElementUI
    object EmptyLine : LegacyElementUI

}