package me.alexandervortex.shelfie.feature.viewer

import me.alexandervortex.shelfie.model.BookNode
import me.alexandervortex.shelfie.model.InlineNode
import me.alexandervortex.shelfie.model.ProgressBookModel
import me.alexandervortex.shelfie.model.RichText
import me.alexandervortex.shelfie.ui.model.BookUIModel
import me.alexandervortex.shelfie.ui.model.StyledText
import me.alexandervortex.shelfie.ui.model.TextStyleUIModel
import me.alexandervortex.shelfie.ui.model.UI
import javax.inject.Inject

class ViewerUIFactory
@Inject constructor() {

    fun getBookUIModel(
        model: ProgressBookModel?
    ): BookUIModel? {
        val result = model?.let {
            BookUIModel(
                id = model.id,
                localPath = model.localPath,
                titleInfo = model.book.titleInfo,
                elements = model.book.document.children.flatMap { mapBookNode(it) }.filterNotNull(),
                progressIndex = model.progress.progressIndex,
                progressOffset = model.progress.progressOffset
            )
        }
        return result
    }

    private fun mapBookNode(node: BookNode): List<UI> {
        return when (node) {
            is BookNode.Section -> mapSection(4, node)
            is BookNode.Paragraph -> mapParagraph(node.content)
            is BookNode.Heading -> mapHeading(node)
            is BookNode.Image -> listOf(UI.Image(node.image))
            is BookNode.Group -> node.children.flatMap { mapBookNode(it) }
            is BookNode.EmptyLine -> listOf(UI.EmptyLine)
        }
    }

    private fun mapParagraph(content: RichText): List<UI> {
        return listOf(
            UI.ComplexText(
                parts = content.parts.mapNotNull { node ->
                    when (node) {
                        is InlineNode.Text -> {
                            val marks = if (node.link != null) {
                                node.marks + TextStyleUIModel.Link(node.link)
                            } else {
                                node.marks
                            }
                            StyledText(marks, node.text)
                        }

                        InlineNode.LineBreak -> StyledText(emptySet(), "\n")
                    }
                }
            )
        )
    }

    private fun mapHeading(node: BookNode.Heading): List<UI> {
        return listOf(
            UI.Heading(
                level = node.level,
                content = UI.ComplexText(
                    parts = node.content.parts.mapNotNull { inline ->
                        (inline as? InlineNode.Text)?.let {
                            StyledText(it.marks, it.text)
                        }
                    }
                )
            )
        )
    }

    private fun mapSection(level: Int, node: BookNode.Section): List<UI> {
        val title = node.title?.let { rich ->
            UI.Heading(
                level = level,
                content = UI.ComplexText(
                    parts = rich.parts.mapNotNull { inline ->
                        (inline as? InlineNode.Text)?.let {
                            StyledText(it.marks, it.text)
                        }
                    }
                )
            )
        }
        val children = node.children.flatMap { mapBookNode(it) }
        return if (title != null) listOf(title) + children else children
    }
}