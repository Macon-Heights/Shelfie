package me.alexandervortex.shelfie.data.parser

import me.alexandervortex.shelfie.base.ext.getBinaries
import me.alexandervortex.shelfie.base.ext.getBody
import me.alexandervortex.shelfie.base.ext.getTitleInfo
import me.alexandervortex.shelfie.data.mapper.ElementMapper
import me.alexandervortex.shelfie.data.mapper.TitleInfoMapper
import me.alexandervortex.shelfie.ui.model.BookUI
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import java.io.File
import javax.inject.Inject

class FictionBookParser
@Inject constructor(
    private val titleInfoMapper: TitleInfoMapper,
    private val elementMapper: ElementMapper,
) {

    fun parse(
        id: String,
        file: File,
        scrollOffset: Int,
        scrollIndex: Int,
    ): BookUI {
        val doc: Document = Jsoup.parse(
            file,
            null,
            "",
            Parser.xmlParser()
        )

        val body = doc.getBody()
        val titleInfo = doc.getTitleInfo()
        val binaries = doc.getBinaries()

        val result = BookUI(
            titleInfo = titleInfoMapper.map(id, file.path, titleInfo),
            elements = elementMapper.map(body, binaries),
            progressIndex = scrollIndex,
            progressOffset = scrollOffset
        )
        return result
    }
}