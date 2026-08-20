package me.alexandervortex.shelfie.feature.viewer

import me.alexandervortex.shelfie.model.BookNode
import me.alexandervortex.shelfie.model.InlineNode
import me.alexandervortex.shelfie.model.ProgressBookModel
import me.alexandervortex.shelfie.model.RichText
import me.alexandervortex.shelfie.ui.model.BookUIModel
import me.alexandervortex.shelfie.ui.model.new.ElementUIModel
import me.alexandervortex.shelfie.ui.model.new.StyledText
import me.alexandervortex.shelfie.ui.model.new.UI
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

    private fun mapBookNode(node: BookNode): List<ElementUIModel?> {
        return when (node) {
            is BookNode.Section -> mapSection(node)
            is BookNode.Paragraph -> mapRichText(node.content)
            is BookNode.Heading -> mapRichText(node.content)
            is BookNode.Image -> listOf(UI.Image(node.image))
            is BookNode.Group -> node.children.flatMap { mapBookNode(it) }
            is BookNode.EmptyLine -> listOf(UI.EmptyLine)
        }
    }

    private fun mapSection(node: BookNode.Section): List<ElementUIModel?> {
        return mapRichText(node.title) + node.children.flatMap { mapBookNode(it) }
    }

    private fun mapRichText(rich: RichText?): List<ElementUIModel> {
        if (rich == null) return emptyList()
        val elements = rich.parts.map { part ->
            mapInlineNode(part)
        }
        return elements
    }

    private fun mapInlineNode(node: InlineNode): ElementUIModel {
        return when (node) {
            is InlineNode.Image -> UI.Image(node.image)
            is InlineNode.LineBreak -> UI.EmptyLine
            is InlineNode.Text ->  UI.ComplexText(
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