package me.alexandervortex.shelfie.model

import me.alexandervortex.shelfie.ui.model.TextStyleUIModel

data class BookDocument(
    val children: List<BookNode>,
)

sealed interface BookNode {

    val id: String

    data class Section(
        override val id: String,
        val title: RichText?,
        val children: List<BookNode>,
    ) : BookNode

    data class Paragraph(
        override val id: String,
        val content: RichText,
    ) : BookNode

    data class Heading(
        override val id: String,
        val level: Int,
        val content: RichText,
    ) : BookNode

    data class Image(
        override val id: String,
        val image: ImageModel,
    ) : BookNode

    data class Group(
        override val id: String,
        val kind: GroupKind,
        val children: List<BookNode>,
    ) : BookNode

    data class EmptyLine(
        override val id: String,
    ) : BookNode
}

sealed interface GroupKind {
    data object Quote : GroupKind
    data object Poem : GroupKind
    data object Stanza : GroupKind
    data object Epigraph : GroupKind

    data class Other(
        val sourceTag: String,
    ) : GroupKind
}

data class RichText(
    val parts: List<InlineNode>,
) {
    val plainText: String
        get() = parts.joinToString("") { part ->
            when (part) {
                is InlineNode.Text -> part.text
                InlineNode.LineBreak -> "\n"
            }
        }
}

sealed interface InlineNode {

    data class Text(
        val text: String,
        val marks: Set<TextStyleUIModel> = emptySet(),
        val link: String? = null,
    ) : InlineNode

    data object LineBreak : InlineNode
}