package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.base.ext.orEmpty
import me.alexandervortex.shelfie.ui.model.ElementUI
import me.alexandervortex.shelfie.ui.model.StyledText
import me.alexandervortex.shelfie.ui.model.TextStyle
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import javax.inject.Inject

class ElementMapper @Inject constructor() {

    fun map(
        element: Element?,
        binaries: Map<String, ByteArray>,
        styles: Set<TextStyle> = emptySet(),
    ): List<ElementUI> {
        if (element == null) return emptyList()

        return when (element.tagName().lowercase()) {
            "image" -> image(element, binaries).orEmpty()
            "empty-line" -> listOf(ElementUI.EmptyLineUI)
            else -> parseComplex(element, binaries, styles)
        }
    }

    private fun parseComplex(
        element: Element,
        binaries: Map<String, ByteArray>,
        styles: Set<TextStyle>,
    ): List<ElementUI> {
        val result = mutableListOf<ElementUI>()
        val textParts = mutableListOf<StyledText>()

        fun flushText() {
            if (textParts.isNotEmpty()) {
                result += ElementUI.TextUI(parts = textParts.toList())
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
                    "strong", "b" -> result += map(node, binaries, styles + TextStyle.Bold)
                    "emphasis", "i" -> result += map(node, binaries, styles + TextStyle.Italic)
                    "u" -> result += map(node, binaries, styles + TextStyle.Underline)
                    "sub" -> result += map(node, binaries, styles + TextStyle.Sub)
                    "sup" -> result += map(node, binaries, styles + TextStyle.Sup)
                    "strike", "s", "del" -> result += map(
                        node,
                        binaries,
                        styles + TextStyle.Custom("strike")
                    )

                    "code", "tt" -> result += map(node, binaries, styles + TextStyle.Monospace)
                    "a" -> {
                        val href = node.attr("href").ifBlank { node.attr("xlink:href") }
                        result += map(node, binaries, styles + TextStyle.Link(href))
                    }

                    "image" -> {
                        flushText()
                        image(node, binaries)?.let { result += it }
                    }

                    "br" -> {
                        textParts += StyledText(styles, "\n")
                    }

                    else -> {
                        // прозрачный контейнер, рекурсивно идём вниз
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
    ): ElementUI? {
        val KEY = "xlink:href"
        val SHARP = "#"
        val ref = element.attr(KEY).removePrefix(SHARP)
        val image = binaries[ref]
        return image?.let { ElementUI.ImageUI(it) }
    }
}