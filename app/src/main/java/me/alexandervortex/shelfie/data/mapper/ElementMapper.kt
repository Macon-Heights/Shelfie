package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.base.Lg
import me.alexandervortex.shelfie.ui.model.ElementUI
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import javax.inject.Inject

class ElementMapper
@Inject constructor() {

    private val lg = Lg("ElementMapper")
    fun map(
        element: Element?,
        binaries: Map<String, ByteArray>,
    ): ElementUI? {
        lg.log("map start")
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

        val result = ElementUI.ContainerElementUI(
            type = element.tagName(),
            elements = children
        )
        lg.log("map end")
        return result
    }

    private fun mapPrimitive(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUI? {
        lg.log("mapPrimitive start")
        val result = when (element.tagName()) {
            "image" -> mapImage(element, binaries)
            "empty-line" -> ElementUI.EmptyLine
            "p", "v", "subtitle" -> ElementUI.TextUI(element.text().trim())
            else -> null
        }
        lg.log("mapPrimitive end")
        return result
    }

    private fun mapImage(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUI? {
        lg.log("mapImage start")
        val ref = element.attr("xlink:href").removePrefix("#")
        val image = binaries[ref]
        val result = image?.let {
            ElementUI.ImageUI(it)
        }
        lg.log("mapImage end")
        return result
    }
}
