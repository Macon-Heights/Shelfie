package me.alexandervortex.shelfie.data.parser

import me.alexandervortex.shelfie.base.ext.getBinaries
import me.alexandervortex.shelfie.base.ext.getBody
import me.alexandervortex.shelfie.base.ext.getTitleInfo
import me.alexandervortex.shelfie.base.ext.normalizeEmptyLines
import me.alexandervortex.shelfie.base.ext.normalizeEmptyTextUI
import me.alexandervortex.shelfie.base.ext.splitPartsBySentences
import me.alexandervortex.shelfie.data.mapper.ElementMapper
import me.alexandervortex.shelfie.data.mapper.TitleInfoMapper
import me.alexandervortex.shelfie.ui.model.BookUIModel
import me.alexandervortex.shelfie.ui.model.TitleInfoUIModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class FictionBookParser
@Inject constructor(
    private val titleInfoMapper: TitleInfoMapper,
    private val elementMapper: ElementMapper,
) {

    fun getPreview(
        inputStream: InputStream,
    ): TitleInfoUIModel {
        val doc: Document = Jsoup.parse(
            inputStream,
            null,
            "",
            Parser.xmlParser()
        )
        val titleInfo = doc.getTitleInfo()
        val binaries = doc.getBinaries()

        val coverImage = getCoverImage(titleInfo, binaries)

        return titleInfoMapper.map(
            coverImage = coverImage,
            titleInfo = titleInfo
        )
    }

    fun parse(
        id: String,
        file: File,
        scrollOffset: Int,
        scrollIndex: Int,
    ): BookUIModel {
        val doc: Document = Jsoup.parse(
            file,
            null,
            "",
            Parser.xmlParser()
        )

        val body = doc.getBody()
        val titleInfo = doc.getTitleInfo()
        val binaries = doc.getBinaries()

        val coverImage = getCoverImage(titleInfo, binaries)

        val result = BookUIModel(
            id = id,
            localPath = file.path,
            titleInfo = titleInfoMapper.map(titleInfo, coverImage),
            elements = elementMapper.map(body, binaries)
                .splitPartsBySentences()
                .normalizeEmptyTextUI()
                .normalizeEmptyLines(),
            progressIndex = scrollIndex,
            progressOffset = scrollOffset
        )
        return result
    }

    private fun getCoverImage(
        titleInfo: Element?,
        binaries: Map<String, ByteArray>,
    ): ByteArray? {
        val coverImageElement = titleInfo
            ?.selectFirst("coverpage > image")
            ?: titleInfo?.selectFirst("coverpage image")

        val ref = coverImageElement
            ?.attr("xlink:href")
            ?.ifBlank { coverImageElement.attr("l:href") }
            ?.ifBlank { coverImageElement.attr("href") }
            ?.removePrefix("#")
            ?.trim()
            .orEmpty()

        if (ref.isBlank()) return null

        return binaries[ref]
    }
}