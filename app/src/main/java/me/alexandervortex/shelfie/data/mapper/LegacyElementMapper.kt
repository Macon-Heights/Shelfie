package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.ui.model.LegacyElementUI
import org.jsoup.nodes.Element
import javax.inject.Inject

@Deprecated("LEGACY")
class LegacyElementMapper
@Inject constructor() {

    fun map(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): LegacyElementUI? {
        return when (element.tagName()) {
            // done
            "section" -> mapSection(element, binaries)
            "image" -> mapImage(element, binaries)
            "empty-line" -> LegacyElementUI.EmptyLine

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
    ): LegacyElementUI.Annotation {
        return LegacyElementUI.Annotation
    }

    private fun mapEpigraph(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): LegacyElementUI.Epigraph {
        return LegacyElementUI.Epigraph
    }

    private fun mapSubtitle(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): LegacyElementUI.Subtitle {
        return LegacyElementUI.Subtitle
    }

    private fun mapSection(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): LegacyElementUI.Section {
        return LegacyElementUI.Section(
            elements = element.children().map { subsection ->
                map(subsection, binaries)
            }
        )
    }

    private fun mapParagraph(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): LegacyElementUI.Paragraph {
        // todo вложенные картинки могут быть, а может даже и другие теги
        return LegacyElementUI.Paragraph(
            text = element.text().trim()
        )
    }

    private fun mapImage(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): LegacyElementUI {
        val ref = element.attr("xlink:href").removePrefix("#")
        val image = binaries[ref]
        return LegacyElementUI.Image(image)
    }

    private fun mapPoem(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): LegacyElementUI.Poem {
        return LegacyElementUI.Poem
    }

    private fun mapCite(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): LegacyElementUI.Cite {
        return LegacyElementUI.Cite
    }

    private fun mapTable(element: Element): LegacyElementUI.Table {
        return LegacyElementUI.Table
    }

    private fun mapTitle(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): LegacyElementUI.Title {
        return LegacyElementUI.Title
    }
}
