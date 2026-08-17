package me.alexandervortex.shelfie.ui.mapper

import me.alexandervortex.shelfie.base.ext.orEmpty
import me.alexandervortex.shelfie.ui.model.ElementUIModel
import me.alexandervortex.shelfie.ui.model.StyledText
import me.alexandervortex.shelfie.ui.model.TextStyleUIModel
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import javax.inject.Inject
import kotlin.collections.plus
import kotlin.collections.plusAssign

class ElementUIMapper @Inject constructor() {

    fun map(
        element: Element?,
        binaries: Map<String, ByteArray>,
        styles: Set<TextStyleUIModel> = emptySet(),
    ): List<ElementUIModel> {
        if (element == null) return emptyList()

        return when (element.tagName().lowercase()) {
            "image", "img" -> image(element, binaries).orEmpty()
            "empty-line" -> listOf(ElementUIModel.EmptyLineUIModel)
            else -> parseComplex(element, binaries, styles)
        }
    }

    private fun parseComplex(
        element: Element,
        binaries: Map<String, ByteArray>,
        styles: Set<TextStyleUIModel>,
    ): List<ElementUIModel> {
        val result = mutableListOf<ElementUIModel>()
        val textParts = mutableListOf<StyledText>()

        fun flushText() {
            if (textParts.isNotEmpty()) {
                result += ElementUIModel.TextUIModel(parts = textParts.toList())
                textParts.clear()
            }
        }

        for (node in element.childNodes()) {
            when (node) {
                is TextNode -> {
                    val text = node.text().trim()
                    if (text.isNotEmpty()) {
                        textParts += StyledText(
                            styles = styles,
                            text = text
                        )
                    }
                }

                is Element -> when (val tag = node.tagName().lowercase()) {
                    "strong", "b" -> result += map(node, binaries, styles + TextStyleUIModel.Bold)
                    "emphasis", "i" -> result += map(node, binaries, styles + TextStyleUIModel.Italic)
                    "u" -> result += map(node, binaries, styles + TextStyleUIModel.Underline)
                    "sub" -> result += map(node, binaries, styles + TextStyleUIModel.Sub)
                    "sup" -> result += map(node, binaries, styles + TextStyleUIModel.Sup)
                    "strike", "s", "del" -> result += map(
                        node,
                        binaries,
                        styles + TextStyleUIModel.Custom("strike")
                    )

                    "code", "tt" -> result += map(node, binaries, styles + TextStyleUIModel.Monospace)
                    "a" -> {
                        val href = node.attr("href").ifBlank { node.attr("xlink:href") }
                        result += map(node, binaries, styles + TextStyleUIModel.Link(href))
                    }

                    "image", "img" -> {
                        flushText()
                        image(node, binaries)?.let { result += it }
                    }

                    "br" -> {
                        textParts += StyledText(styles, "\n")
                    }

                    else -> {
                        result += map(node, binaries, styles)
                    }
                }

                else -> Unit
            }
        }

        flushText()
        return result
    }

    private fun image(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUIModel? {
        val ref = element.attr("src")
            .ifBlank { element.attr("xlink:href") }
            .ifBlank { element.attr("l:href") }
            .ifBlank { element.attr("href") }
            .removePrefix("#")
            .trim()
        val image = binaries[ref] ?: binaries[ref.substringAfterLast("/")]
        return image?.let { ElementUIModel.ImageUIModel(it) }
    }
}