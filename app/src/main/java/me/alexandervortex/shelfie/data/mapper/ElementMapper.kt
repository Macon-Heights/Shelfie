package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.ui.model.ElementUI
import org.jsoup.nodes.Element
import javax.inject.Inject

class ElementMapper
@Inject constructor() {

    fun map(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUI? {
        return when (element.tagName()) {
            // done
            "section" -> mapSection(element, binaries)
            "image" -> mapImage(element, binaries)
            "empty-line" -> ElementUI.EmptyLine

            // needs work
            "title" -> mapTitle(element, binaries)
            "epigraph" -> mapEpigraph(element, binaries)

            "annotation" -> mapAnnotation(element, binaries)
            "p" -> mapParagraph(element, binaries)
            "poem" -> mapPoem(element, binaries)
            "cite" -> mapCite(element, binaries)
            "table" -> mapTable(element)
            "subtitle" -> mapSubtitle(element, binaries)


            else -> null
        }
    }

    private fun mapAnnotation(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUI.Annotation {
        return ElementUI.Annotation
    }

    private fun mapEpigraph(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUI.Epigraph {
        return ElementUI.Epigraph
    }

    private fun mapSubtitle(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUI.Subtitle {
        return ElementUI.Subtitle
    }

    private fun mapSection(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUI.Section {
        return ElementUI.Section(
            elements = element.children().map { subsection ->
                map(subsection, binaries)
            }
        )
    }

    private fun mapParagraph(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUI.Paragraph {
        // todo вложенные картинки могут быть, а может даже и другие теги
        return ElementUI.Paragraph(
            text = element.text().trim()
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

    private fun mapPoem(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUI.Poem {
        return ElementUI.Poem
    }

    private fun mapCite(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUI.Cite {
        return ElementUI.Cite
    }

    private fun mapTable(element: Element): ElementUI.Table {
        return ElementUI.Table
    }

    private fun mapTitle(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUI.Title {
        return ElementUI.Title
    }
}
