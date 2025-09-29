package me.alexandervortex.shelfie.data.parser

import android.util.Base64
import me.alexandervortex.shelfie.data.model.BlockUi
import me.alexandervortex.shelfie.data.model.BookFile
import me.alexandervortex.shelfie.data.model.BookUi
import me.alexandervortex.shelfie.data.model.SectionUi
import me.alexandervortex.shelfie.data.model.StanzaUi
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import javax.inject.Inject

class FictionBookParser
@Inject constructor() {

    fun parse(
        file: BookFile,
        id: String,
    ): BookUi {

        val localPath = file.path

        val doc = Jsoup.parse(
            file.file,
            null,
            "",
            Parser.xmlParser()
        )

        val description = doc.selectFirst("description")

        val title = description?.selectFirst("book-title")?.text()?.trim()
            ?: file.file.nameWithoutExtension

        val year = description?.selectFirst("year")?.text()?.trim()

        val author = description?.selectFirst("author")?.text()?.trim()

        val annotation = description?.selectFirst("annotation")?.text()?.trim()

        // Cover
        val coverHref = description
            ?.selectFirst("coverpage image")
            ?.attr("xlink:href")
            ?.removePrefix("#")

        val binaries = doc.select("binary").associate {
            val id = it.attr("id")
            val base64 = it.text().trim()
            id to Base64.decode(base64, Base64.DEFAULT)
        }

        val coverImage = coverHref?.let { binaries[it] }

        val sections = doc.select("body > section").map { mapSection(it, binaries) }

        return BookUi(
            id = id,
            localPath = localPath,
            year = year,
            title = title,
            author = author,
            annotation = annotation,
            coverImage = coverImage,
            sections = sections
        )
    }

    private fun mapSection(section: Element, binaries: Map<String, ByteArray>): SectionUi {
        val title = section.selectFirst("> title p")?.text()
        val blocks = mutableListOf<BlockUi>()

        for (child in section.children()) {
            when (child.tagName()) {
                "p" -> blocks.add(BlockUi.Paragraph(child.text()))
                "empty-line" -> blocks.add(BlockUi.EmptyLine)
                "poem" -> blocks.add(mapPoem(child))
                "cite" -> blocks.add(mapCite(child, binaries))
                "table" -> blocks.add(mapTable(child))
                "image" -> {
                    val ref = child.attr("xlink:href").removePrefix("#")
                    binaries[ref]?.let { blocks.add(BlockUi.Image(it)) }
                }

                "section" -> {
                    // вложенные секции → разворачиваем как отдельные блоки
                    blocks.addAll(mapSection(child, binaries).blocks)
                }
            }
        }

        return SectionUi(title, blocks)
    }

    private fun mapPoem(poem: Element): BlockUi.Poem {
        val title = poem.selectFirst("title p")?.text()
        val stanzas = poem.select("stanza").map { stanza ->
            val verses = stanza.select("v").map { it.text() }
            StanzaUi(verses)
        }
        val author = poem.selectFirst("text-author")?.text()
        return BlockUi.Poem(title, stanzas, author)
    }

    private fun mapCite(cite: Element, binaries: Map<String, ByteArray>): BlockUi.Cite {
        val blocks = mutableListOf<BlockUi>()
        for (child in cite.children()) {
            when (child.tagName()) {
                "p" -> blocks.add(BlockUi.Paragraph(child.text()))
                "poem" -> blocks.add(mapPoem(child))
                "image" -> {
                    val ref = child.attr("xlink:href").removePrefix("#")
                    binaries[ref]?.let { blocks.add(BlockUi.Image(it)) }
                }
            }
        }
        val author = cite.selectFirst("text-author")?.text()
        return BlockUi.Cite(blocks, author)
    }

    private fun mapTable(table: Element): BlockUi.Table {
        val rows = table.select("tr").map { row ->
            row.select("td").map { it.text() }
        }
        return BlockUi.Table(rows)
    }
}