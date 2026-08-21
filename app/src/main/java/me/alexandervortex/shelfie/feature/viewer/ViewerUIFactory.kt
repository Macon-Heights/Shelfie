package me.alexandervortex.shelfie.feature.viewer

import me.alexandervortex.shelfie.model.BookNode
import me.alexandervortex.shelfie.model.InlineNode
import me.alexandervortex.shelfie.model.ProgressBookModel
import me.alexandervortex.shelfie.model.RichText
import me.alexandervortex.shelfie.ui.model.BookUIModel
import me.alexandervortex.shelfie.ui.model.StyledText
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
            is BookNode.Paragraph -> mapRichText(node.content)
            is BookNode.Heading -> mapHeading(node)
            is BookNode.Image -> listOf(UI.Image(node.image))
            is BookNode.Group -> node.children.flatMap { mapBookNode(it) }
            is BookNode.EmptyLine -> listOf(UI.EmptyLine)
        }
    }

    private fun mapHeading(node: BookNode.Heading): List<UI> {
        return mapRichText(node.content).wrapWith { content ->
            UI.Heading(
                level = node.level,
                content = content
            )
        }
    }

    private fun mapSection(level: Int, node: BookNode.Section): List<UI> {
        return mapRichText(node.title).wrapWith { content ->
            UI.Heading(
                level = level,
                content = content
            )
        } + node.children.flatMap { mapBookNode(it) }
    }

    private fun List<UI>.wrapWith(content: (UI.ComplexText) -> UI): List<UI> {
        return this.map {
            if (it is UI.ComplexText) {
                content.invoke(it)
            } else {
                it
            }
        }
    }

    private fun mapRichText(rich: RichText?): List<UI> {
        if (rich == null) return emptyList()
        val elements = rich.parts.map { part ->
            mapInlineNode(part)
        }
        return elements
    }

    private fun mapInlineNode(node: InlineNode): UI {
        return when (node) {
            is InlineNode.Image -> UI.Image(node.image)
            is InlineNode.LineBreak -> UI.EmptyLine
            is InlineNode.Text -> UI.ComplexText(
                listOf(
                    StyledText(
                        node.marks,
                        node.text
                    )
                )
            )
        }
    }
}