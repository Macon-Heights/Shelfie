package me.alexandervortex.shelfie.ui.model

// Универсальный блок текста / изображения
sealed class BlockUi {

    data class Paragraph(
        val text: String,
    ) : BlockUi()

    data class Subtitle(val html: String) : BlockUi()

    data object EmptyLine : BlockUi()

    data class Poem(
        val title: String?,
        val stanzas: List<StanzaUi>,
        val author: String?,
    ) : BlockUi()

    data class Cite(
        val blocks: List<BlockUi>,
        val author: String?,
    ) : BlockUi()

    data class Table(
        val rows: List<List<String>>,
    ) : BlockUi()

    data class Image(
        val data: ByteArray,
    ) : BlockUi()
}

data class StanzaUi(
    val verses: List<String>,
)