package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.model.BookDocument
import me.alexandervortex.shelfie.model.BookNode
import me.alexandervortex.shelfie.model.ByteImageModel
import me.alexandervortex.shelfie.model.GroupKind
import me.alexandervortex.shelfie.model.InlineNode
import me.alexandervortex.shelfie.model.RichText
import me.alexandervortex.shelfie.ui.model.TextStyleUIModel
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import javax.inject.Inject

class ElementMapper
@Inject constructor() {

    fun map(
        root: Element?,
        binaries: Map<String, ByteArray>,
    ): BookDocument {
        if (root == null) {
            return BookDocument(emptyList())
        }

        val result = BookDocument(
            children = mapNodes(
                nodes = root.childNodes(),
                binaries = binaries,
                parentPath = "root",
            )
        )
        return result
    }

    private fun mapNodes(
        nodes: List<Node>,
        binaries: Map<String, ByteArray>,
        parentPath: String,
    ): List<BookNode> {

        val result = mutableListOf<BookNode>()
        val pendingInline = mutableListOf<InlineNode>()

        fun flushInline() {
            if (pendingInline.isEmpty()) return

            val content = compactInline(pendingInline)

            if (content != null && content.plainText.isNotBlank()) {
                result += BookNode.Paragraph(
                    id = "$parentPath/text-${result.size}",
                    content = content,
                )
            }

            pendingInline.clear()
        }

        nodes.forEachIndexed { index, node ->

            val path = "$parentPath/$index"

            when (node) {

                is TextNode -> {
                    pendingInline += mapTextNode(
                        node = node,
                        marks = emptySet(),
                        link = null,
                    )
                }

                is Element -> {
                    if (isBlockElement(node)) {
                        flushInline()

                        mapBlock(
                            element = node,
                            binaries = binaries,
                            path = path,
                        )?.let(result::add)
                    } else {
                        pendingInline += mapInlineElement(
                            element = node,
                            binaries = binaries,
                            marks = emptySet(),
                            link = null,
                        )
                    }
                }
            }
        }

        flushInline()

        return result
    }

    private fun mapBlock(
        element: Element,
        binaries: Map<String, ByteArray>,
        path: String,
    ): BookNode? {

        val tag = element.tagName().lowercase()
        val classes = element.classNames().map { it.lowercase() } // todo
        val id = elementId(element, path)

        fun isType(type: String) = tag == type || classes.any { it.contains(type) } // todo

        return when {

            tag == "section" || tag == "article" -> {
                mapSection(
                    element = element,
                    binaries = binaries,
                    path = path,
                )
            }

            isType("subtitle") -> {
                mapRichText(element, binaries)?.let {
                    BookNode.Heading(
                        id = id,
                        level = 2,
                        content = it,
                    )
                }
            }

            isType("title") && !tag.startsWith("h") -> {
                mapTitle(element, binaries)?.let {
                    BookNode.Heading(
                        id = id,
                        level = 1,
                        content = it,
                    )
                }
            }

            tag.matches(Regex("h[1-6]")) -> {
                mapRichText(element, binaries)?.let {
                    BookNode.Heading(
                        id = id,
                        level = tag.substring(1).toInt(),
                        content = it,
                    )
                }
            }

            tag == "p" || classes.any { it == "p" || it == "paragraph" } -> {
                mapRichText(element, binaries)?.let {
                    BookNode.Paragraph(
                        id = id,
                        content = it
                    )
                }
            }

            tag == "image" || tag == "img" -> {
                image(element, binaries)?.let {
                    BookNode.Image(
                        id = id,
                        image = it,
                    )
                }
            }

            tag == "empty-line" -> {
                BookNode.EmptyLine(id)
            }

            tag == "blockquote" || tag == "cite" -> {
                mapGroup(
                    element = element,
                    binaries = binaries,
                    path = path,
                    kind = GroupKind.Quote,
                )
            }

            tag == "poem" -> {
                mapGroup(
                    element = element,
                    binaries = binaries,
                    path = path,
                    kind = GroupKind.Poem,
                )
            }

            tag == "stanza" -> {
                mapGroup(
                    element = element,
                    binaries = binaries,
                    path = path,
                    kind = GroupKind.Stanza,
                )
            }

            tag == "epigraph" -> {
                mapGroup(
                    element = element,
                    binaries = binaries,
                    path = path,
                    kind = GroupKind.Epigraph,
                )
            }

            else -> {
                mapGroup(
                    element = element,
                    binaries = binaries,
                    path = path,
                    kind = GroupKind.Other(tag),
                )
            }
        }
    }

    private fun mapSection(
        element: Element,
        binaries: Map<String, ByteArray>,
        path: String,
    ): BookNode.Section {

        val titleElement = element.children()
            .firstOrNull { child ->
                val tag = child.tagName().lowercase()

                tag == "title" ||
                        tag == "subtitle" ||
                        tag.matches(Regex("h[1-6]"))
            }

        val title = titleElement?.let {
            mapTitle(it, binaries)
        }

        val children = mapNodes(
            nodes = element.childNodes()
                .filterNot { it === titleElement },
            binaries = binaries,
            parentPath = path,
        )

        return BookNode.Section(
            id = elementId(element, path),
            title = title,
            children = children,
        )
    }

    private fun mapGroup(
        element: Element,
        binaries: Map<String, ByteArray>,
        path: String,
        kind: GroupKind,
    ): BookNode? {
        val children = mapNodes(
            nodes = element.childNodes(),
            binaries = binaries,
            parentPath = path,
        )

        return if (kind is GroupKind.Other && children.size < 2) {
            children.firstOrNull()
        } else {
            BookNode.Group(
                id = elementId(element, path),
                kind = kind,
                children = children,
            )
        }
    }

    private fun mapRichText(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): RichText? {
        return compactInline(
            mapInlineNodes(
                nodes = element.childNodes(),
                binaries = binaries,
                marks = emptySet(),
                link = null,
            )
        )
    }

    private fun mapTitle(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): RichText? {

        val paragraphs = element.children()
            .filter { it.tagName().equals("p", ignoreCase = true) }

        if (paragraphs.isEmpty()) {
            return mapRichText(element, binaries)
        }

        val result = mutableListOf<InlineNode>()

        paragraphs.forEachIndexed { index, paragraph ->

            result += mapInlineNodes(
                nodes = paragraph.childNodes(),
                binaries = binaries,
                marks = emptySet(),
                link = null,
            )

            if (index != paragraphs.lastIndex) {
                result += InlineNode.LineBreak
            }
        }

        return compactInline(result)
    }

    private fun mapInlineNodes(
        nodes: List<Node>,
        binaries: Map<String, ByteArray>,
        marks: Set<TextStyleUIModel>,
        link: String?,
    ): List<InlineNode> {

        val result = mutableListOf<InlineNode>()

        nodes.forEach { node ->
            when (node) {

                is TextNode -> {
                    if (node.text().trim().isNotEmpty()) result += mapTextNode(
                        node = node,
                        marks = marks,
                        link = link,
                    )
                }

                is Element -> {
                    result += mapInlineElement(
                        element = node,
                        binaries = binaries,
                        marks = marks,
                        link = link,
                    )
                }
            }
        }

        return result
    }

    private fun mapInlineElement(
        element: Element,
        binaries: Map<String, ByteArray>,
        marks: Set<TextStyleUIModel>,
        link: String?,
    ): List<InlineNode> {

        val tag = element.tagName().lowercase()

        return when (tag) {

            "strong", "b" ->
                mapInlineNodes(
                    element.childNodes(),
                    binaries,
                    marks + TextStyleUIModel.Bold,
                    link,
                )

            "emphasis", "em", "i" ->
                mapInlineNodes(
                    element.childNodes(),
                    binaries,
                    marks + TextStyleUIModel.Italic,
                    link,
                )

            "u" ->
                mapInlineNodes(
                    element.childNodes(),
                    binaries,
                    marks + TextStyleUIModel.Underline,
                    link,
                )

            "sub" ->
                mapInlineNodes(
                    element.childNodes(),
                    binaries,
                    marks + TextStyleUIModel.Sub,
                    link,
                )

            "sup" ->
                mapInlineNodes(
                    element.childNodes(),
                    binaries,
                    marks + TextStyleUIModel.Sup,
                    link,
                )

            "strike", "s", "del" ->
                mapInlineNodes(
                    element.childNodes(),
                    binaries,
                    marks + TextStyleUIModel.Strikethrough,
                    link,
                )

            "code", "tt" ->
                mapInlineNodes(
                    element.childNodes(),
                    binaries,
                    marks + TextStyleUIModel.Monospace,
                    link,
                )

            "a" -> {
                val href = href(element)

                mapInlineNodes(
                    nodes = element.childNodes(),
                    binaries = binaries,
                    marks = marks,
                    link = href ?: link,
                )
            }

            "br" -> {
                listOf(InlineNode.LineBreak)
            }

            else -> {
                // Unknown inline elements are transparent.
                // Their children are still preserved.
                mapInlineNodes(
                    nodes = element.childNodes(),
                    binaries = binaries,
                    marks = marks,
                    link = link,
                )
            }
        }
    }

    private fun mapTextNode(
        node: TextNode,
        marks: Set<TextStyleUIModel>,
        link: String?,
    ): InlineNode.Text {

        return InlineNode.Text(
            text = node.text(),
            marks = marks,
            link = link,
        )
    }

    private fun image(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ByteImageModel? {

        val ref = element.attr("src")
            .ifBlank { element.attr("xlink:href") }
            .ifBlank { element.attr("l:href") }
            .ifBlank { element.attr("href") }
            .removePrefix("#")
            .trim()

        if (ref.isBlank()) return null

        val bytes = binaries[ref]
            ?: binaries[ref.substringAfterLast("/")]

        return bytes?.let {
            ByteImageModel(it)
        }
    }

    private fun href(
        element: Element,
    ): String? {

        return element.attr("href")
            .ifBlank { element.attr("xlink:href") }
            .ifBlank { element.attr("l:href") }
            .trim()
            .takeIf { it.isNotEmpty() }
    }

    private fun elementId(
        element: Element,
        path: String,
    ): String {

        return element.attr("id")
            .ifBlank { element.attr("xml:id") }
            .ifBlank { "node:$path" }
    }

    private fun isBlockElement(
        element: Element,
    ): Boolean {
        val tag = element.tagName().lowercase()
        val classes = element.classNames().map { it.lowercase() }

        if (tag in BLOCK_TAGS || tag.matches(Regex("h[1-6]"))) {
            return true
        }

        if (classes.any { it.contains("title") || it.contains("subtitle") || it == "p" || it == "paragraph" }) {
            return true
        }

        return element.children().any { isBlockElement(it) }
    }

    private fun compactInline(
        source: List<InlineNode>,
    ): RichText? {
        if (source.isEmpty()) return null
        val result = mutableListOf<InlineNode>()

        source.forEach { node ->
            if (node is InlineNode.Text && node.text.isEmpty()) return@forEach

            val previous = result.lastOrNull()

            if (
                previous is InlineNode.Text &&
                node is InlineNode.Text &&
                previous.marks == node.marks &&
                previous.link == node.link
            ) {
                result[result.lastIndex] = previous.copy(
                    text = previous.text + node.text
                )
            } else {
                result += node
            }
        }

        return if (result.isNotEmpty()) {
            RichText(parts = result)
        } else null
    }

    private companion object {

        val BLOCK_TAGS = setOf(
            "section",
            "article",
            "div",
            "p",
            "title",
            "subtitle",
            "image",
            "img",
            "empty-line",
            "blockquote",
            "cite",
            "poem",
            "stanza",
            "epigraph",
        )
    }
}