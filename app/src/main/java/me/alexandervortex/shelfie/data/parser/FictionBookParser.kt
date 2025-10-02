package me.alexandervortex.shelfie.data.parser

import me.alexandervortex.shelfie.base.getBinaries
import me.alexandervortex.shelfie.base.getBody
import me.alexandervortex.shelfie.base.getTitleInfo
import me.alexandervortex.shelfie.data.mapper.ElementMapper
import me.alexandervortex.shelfie.data.mapper.TitleInfoMapper
import me.alexandervortex.shelfie.data.model.BookFile
import me.alexandervortex.shelfie.ui.model.BookUI
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import javax.inject.Inject

class FictionBookParser
@Inject constructor(
    private val titleInfoMapper: TitleInfoMapper,
    private val elementMapper: ElementMapper,
) {

    fun parse(
        id: String,
        file: BookFile,
    ): BookUI {
        val doc: Document = Jsoup.parse(
            file.file,
            null,
            "",
            Parser.xmlParser()
        )

        val body = doc.getBody()
        val titleInfo = doc.getTitleInfo()
        val binaries = doc.getBinaries()

        val result = BookUI(
            titleInfo = titleInfoMapper.map(id, file.path, titleInfo),
            elements = elementMapper.map(body, binaries)
        )
        return result
    }
}