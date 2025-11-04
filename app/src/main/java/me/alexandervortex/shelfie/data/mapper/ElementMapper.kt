package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.base.ext.isNoChildren
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
    ): List<ElementUI> {
        if (element == null) return emptyList()

        if (element.isNoChildren()) {
            return primitive(element, binaries).orEmpty()
        }

        /*
        <p>
            <strong>Infogrid Pacific</strong> - Element (TextNode) -> StyledText
            Pte. Ltd.  - TextNode -> StyledText
        </p>
         */

        val children = element.childNodes()
            .flatMap { node ->
                when (node) {
                    is Element -> map(node, binaries) // прыгаем в рекурсию
                    is TextNode -> node.text()
                        .takeIf { it.trim().isNotEmpty() }
                        ?.let {
                            listOf(
                                ElementUI.TextUI(parts = listOf(StyledText(TextStyle.Normal, it)))
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
            "image" -> getImage(element, binaries)
            "empty-line" -> ElementUI.EmptyLineUI
            else -> { // "p", "v", "subtitle" ?? какие еще теги стоит обработать
                if (element.text().trim().isEmpty()) {
                    // element.tagName.toTextStyle
                    ElementUI.TextUI(
                        listOf(StyledText(TextStyle.fromTag(element.tag()), element.text()))
                    )
                } else {
                    null
                }
            }
        }
    }

    // its fine
    private fun getImage(
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