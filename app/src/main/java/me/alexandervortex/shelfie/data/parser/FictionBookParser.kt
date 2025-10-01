package me.alexandervortex.shelfie.data.parser

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
        val titleInfo = doc.selectFirst("description > title-info")
        return BookUI(
            titleInfo = titleInfoMapper.map(
                id,
                file.path,
                titleInfo
            ),
            elements = elementMapper.map()
        )
    }
}