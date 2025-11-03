package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.base.ext.isPrimitive
import me.alexandervortex.shelfie.base.ext.orEmpty
import me.alexandervortex.shelfie.ui.model.ElementUI
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import javax.inject.Inject

const val SENTENCE_SEPARATOR = ". "

class ElementMapper
@Inject constructor() {

    fun map(
        element: Element?,
        binaries: Map<String, ByteArray>,
    ): List<ElementUI> {
        if (element == null) return emptyList()

        if (element.isPrimitive()) {
            return primitive(element, binaries).orEmpty()
        }

        val children = element.childNodes().flatMap { node ->
            when (node) {
                is Element -> map(node, binaries)
                is TextNode -> node.text().trim()
                    .takeIf { it.isNotEmpty() }
                    ?.let {
                        listOf(
                            ElementUI.TextUI(
                                parts = it.split(SENTENCE_SEPARATOR)
                                    .map { it.trim() }
                                    .filter { it.isNotBlank() }
                            )
                        )
                    }
                    ?: emptyList()

                else -> emptyList()
            }
        }

        return children
    }

    private fun primitive(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUI? {
        return when (element.tagName()) {
            "image" -> mapImage(element, binaries)
            "empty-line" -> ElementUI.EmptyLine
            "p", "v", "subtitle" -> ElementUI.TextUI(
                element.text().trim().split(SENTENCE_SEPARATOR)
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
            )

            else -> null
        }
    }

    private fun mapImage(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUI? {
        val ref = element.attr("xlink:href").removePrefix("#")
        val image = binaries[ref]
        return image?.let { ElementUI.ImageUI(it) }
    }
}
