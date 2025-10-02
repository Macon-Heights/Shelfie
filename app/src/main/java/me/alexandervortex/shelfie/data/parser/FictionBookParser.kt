package me.alexandervortex.shelfie.data.parser

import android.util.Base64
import me.alexandervortex.shelfie.base.Lg
import me.alexandervortex.shelfie.data.mapper.ElementMapper
import me.alexandervortex.shelfie.data.mapper.TitleInfoMapper
import me.alexandervortex.shelfie.data.model.BookFile
import me.alexandervortex.shelfie.ui.model.BookUI
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import javax.inject.Inject

class FictionBookParser
@Inject constructor(
    private val titleInfoMapper: TitleInfoMapper,
    private val elementMapper: ElementMapper,
) {

    fun parse(
        file: BookFile,
        id: String,
    ): BookUI {
        val doc = Jsoup.parse(
            file.file,
            null,
            "",
            Parser.xmlParser()
        )
        val body = doc.selectFirst("body")
        val titleInfo = doc.selectFirst("description > title-info")
        val binaries = doc.select("binary")
            .associate {
                val binaryId = it.attr("id")
                val base64 = it.text().trim()
                binaryId to Base64.decode(base64, Base64.DEFAULT)
            }

        val result = BookUI(
            titleInfo = titleInfoMapper.map(
                id,
                file.path,
                titleInfo
            ),
            elements = body?.children()?.mapNotNull { element ->
                elementMapper.map(element, binaries)
            }.orEmpty()
        )
        return result
    }
}