package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.ui.model.ElementUI
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import javax.inject.Inject

class ElementMapper
@Inject constructor() {

    fun map(
        element: Element?,
        binaries: Map<String, ByteArray>,
    ): ElementUI? {
        if (element == null) return null

        if (element.childNodes().isEmpty()) {
            return mapPrimitive(element, binaries)
        }

        val children = element.childNodes().mapNotNull { node ->
            when (node) {
                is Element -> map(node, binaries) // рекурсия
                is TextNode -> node.text().trim()
                    .takeIf { it.isNotEmpty() }
                    ?.let { ElementUI.TextUI(it) }

                else -> null
            }
        }

        return ElementUI.ContainerElementUI(
            type = element.tagName(),
            elements = children
        )
    }

    private fun mapPrimitive(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUI? {
        return when (element.tagName()) {
            "image" -> mapImage(element, binaries)
            "empty-line" -> ElementUI.EmptyLine
            "p", "v", "subtitle" -> ElementUI.TextUI(element.text().trim())
            else -> null
        }
    }

    private fun mapImage(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUI? {
        val ref = element.attr("xlink:href").removePrefix("#")
        val image = binaries[ref]
        return image?.let {
            ElementUI.ImageUI(it)
        }
    }
}
