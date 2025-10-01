package me.alexandervortex.shelfie.data.parser

import android.util.Base64
import me.alexandervortex.shelfie.data.mapper.Fb2Mapper
import me.alexandervortex.shelfie.data.model.BookFile
import me.alexandervortex.shelfie.ui.model.BookUI
import me.alexandervortex.shelfie.ui.model.TitleInfoUI
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import javax.inject.Inject

class FictionBookParser
@Inject constructor(
    private val fb2mapper: Fb2Mapper,
) {

    fun parse(
        file: BookFile,
        id: String,
    ): BookUI {

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

        val sections = doc.select("body > section").map { fb2mapper.mapSection(it, binaries) }

        val book = BookUI(
            titleInfo = TitleInfoUI(
                id = id,
                localPath = localPath,
                year = year,
                title = title,
                author = author,
                annotation = annotation,
                coverImage = coverImage,
                genre = "genre"
            ),
            sections = sections,
        )

        return book
    }

}