package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.ui.model.BlockUi
import me.alexandervortex.shelfie.ui.model.SectionUi
import me.alexandervortex.shelfie.ui.model.StanzaUi
import org.jsoup.nodes.Element
import javax.inject.Inject

class Fb2Mapper
@Inject constructor() {

    fun mapSection(section: Element, binaries: Map<String, ByteArray>): SectionUi {
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

    fun mapPoem(poem: Element): BlockUi.Poem {
        val title = poem.selectFirst("title p")?.text()
        val stanzas = poem.select("stanza").map { stanza ->
            val verses = stanza.select("v").map { it.text() }
            StanzaUi(verses)
        }
        val author = poem.selectFirst("text-author")?.text()
        return BlockUi.Poem(title, stanzas, author)
    }

    fun mapCite(cite: Element, binaries: Map<String, ByteArray>): BlockUi.Cite {
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

    fun mapTable(table: Element): BlockUi.Table {
        val rows = table.select("tr").map { row ->
            row.select("td").map { it.text() }
        }
        return BlockUi.Table(rows)
    }
}
