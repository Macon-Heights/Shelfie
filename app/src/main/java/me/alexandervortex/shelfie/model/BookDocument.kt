package me.alexandervortex.shelfie.model

data class BookDocument(
    val children: List<BookNode>,
)

sealed interface BookNode {

    /**
     * Stable source id if the format provides one,
     * otherwise deterministic id generated from the tree position.
     */
    val id: String

    /**
     * Semantic book section/chapter.
     *
     * Sections may contain other sections.
     */
    data class Section(
        override val id: String,
        val title: RichText?,
        val children: List<BookNode>,
    ) : BookNode

    /**
     * Regular text paragraph.
     */
    data class Paragraph(
        override val id: String,
        val content: RichText,
    ) : BookNode

    /**
     * Heading which is not represented by a Section itself.
     */
    data class Heading(
        override val id: String,
        val level: Int,
        val content: RichText,
    ) : BookNode

    /**
     * Block-level image.
     */
    data class Image(
        override val id: String,
        val image: ImageModel,
    ) : BookNode

    /**
     * Semantic nested container which should retain its hierarchy,
     * but is not a chapter.
     */
    data class Group(
        override val id: String,
        val kind: GroupKind,
        val children: List<BookNode>,
    ) : BookNode

    /**
     * Explicit empty line from the source document.
     */
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
                is InlineNode.Image -> ""
                InlineNode.LineBreak -> "\n"
            }
        }
}

sealed interface InlineNode {

    data class Text(
        val text: String,
        val marks: Set<TextMark> = emptySet(),
        val link: String? = null,
    ) : InlineNode

    data class Image(
        val image: ImageModel,
    ) : InlineNode

    data object LineBreak : InlineNode
}

enum class TextMark {
    Bold,
    Italic,
    Underline,
    Subscript,
    Superscript,
    Strikethrough,
    Monospace,
}

data class ImageModel(
    val image: ByteArray? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageModel

        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        return image?.contentHashCode() ?: 0
    }
}