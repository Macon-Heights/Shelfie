package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.base.ext.orEmpty
import me.alexandervortex.shelfie.ui.model.ElementUI
import me.alexandervortex.shelfie.ui.model.StyledText
import me.alexandervortex.shelfie.ui.model.TextStyle
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import javax.inject.Inject

// todo в самом конце склеить все EmptyLine которые идут подряд друг за другом

class ElementMapper @Inject constructor() {

    fun map(
        element: Element?,
        binaries: Map<String, ByteArray>,
        styles: Set<TextStyle> = emptySet(),
    ): List<ElementUI> {
        if (element == null) return emptyList()

        when (element.tagName()) {
            "image" -> {
                return image(element, binaries).orEmpty()
            }

            "empty-line" -> {
                return listOf(ElementUI.EmptyLineUI)
            }

            else -> if (element.text().trim().isNotEmpty()) {
                ElementUI.TextUI(
                    listOf(StyledText(styles, element.text())),
                )
            }
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
                    is Element -> map(node, binaries, styles) // прыгаем в рекурсию
                    is TextNode -> node.text()
                        .takeIf { it.trim().isNotEmpty() }
                        ?.let {
                            listOf(
                                ElementUI.TextUI(parts = listOf(StyledText(styles, it)))
                            )
                        }
                        ?: emptyList()

                    else -> emptyList()
                }
            }

        return children
    }

    // its fine
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