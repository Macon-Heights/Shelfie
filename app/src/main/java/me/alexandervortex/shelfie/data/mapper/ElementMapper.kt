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

        // Блоки без вложенности
        // Сюда же мы будем попадать рекурсивно, когда дойдем до конца ветки
        if (element.isNoChildren()) {
            return primitive(element, binaries).orEmpty()
        }

        // допустим <p>
        //
        // Этотекст
        // <strong>asd<strong>
        // и еще текст
        //
        // <p>

        val children = element.childNodes()
            .flatMap { node ->
                when (node) {
                    is Element -> map(node, binaries) // прыгаем в рекурсию
                    is TextNode -> node.text()
                        .takeIf { it.isNotEmpty() }
                        ?.let {
                            listOf(
                                ElementUI.TextUI(
                                    parts = listOf(StyledText(TextStyle.Normal, it))
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
            "empty-line" -> ElementUI.EmptyLineUI
            "p", "v", "subtitle" -> ElementUI.TextUI( // какие еще теги стоит обработать
                listOf(StyledText(TextStyle.Normal, element.text()))
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
