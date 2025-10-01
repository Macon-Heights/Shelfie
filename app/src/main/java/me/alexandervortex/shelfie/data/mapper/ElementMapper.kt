package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.ui.model.ElementUI
import org.jsoup.nodes.Element
import javax.inject.Inject

class ElementMapper
@Inject constructor() {

    fun map(
        element: Element?,
        binaries: Map<String, ByteArray>,
    ): ElementUI? {
        return when {
            element == null -> null
            element.tagName() == "image" -> mapImage(element, binaries)
            element.childNodes().isNotEmpty() -> mapStack(element, binaries)
            element.childNodes().isEmpty() -> mapText(element)
            else -> null
        }
    }

    private fun mapText(
        element: Element,
    ): ElementUI.Paragraph {
        return ElementUI.Paragraph(element.text().trim())
    }

    private fun mapStack(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUI? {
        return ElementUI.Container(
            type = element.tagName(),
            children = element.children().mapNotNull { child ->
                map(child, binaries)
            }
        )
    }

    private fun mapImage(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUI {
        val ref = element.attr("xlink:href").removePrefix("#")
        val image = binaries[ref]
        return ElementUI.Image(image)
    }
}
