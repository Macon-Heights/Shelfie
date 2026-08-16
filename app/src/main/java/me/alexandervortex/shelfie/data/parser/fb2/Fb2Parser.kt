package me.alexandervortex.shelfie.data.parser.fb2

import me.alexandervortex.shelfie.base.ext.getBinaries
import me.alexandervortex.shelfie.base.ext.getBody
import me.alexandervortex.shelfie.base.ext.getTitleInfo
import me.alexandervortex.shelfie.base.ext.normalizeEmptyLines
import me.alexandervortex.shelfie.base.ext.normalizeEmptyTextUI
import me.alexandervortex.shelfie.base.ext.splitPartsBySentences
import me.alexandervortex.shelfie.data.mapper.ElementMapper
import me.alexandervortex.shelfie.data.mapper.PreviewBookMapper
import me.alexandervortex.shelfie.model.ImageModel
import me.alexandervortex.shelfie.model.ParsedBookModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import java.io.InputStream
import javax.inject.Inject

class Fb2Parser
@Inject constructor(
    private val previewBookMapper: PreviewBookMapper,
    private val elementMapper: ElementMapper,
) {

    fun parse(
        inputStream: InputStream
    ): ParsedBookModel {
        val doc: Document = Jsoup.parse(
            inputStream,
            null,
            "",
            Parser.xmlParser()
        )

        val body = doc.getBody()
        val titleInfo = doc.getTitleInfo()
        val binaries = doc.getBinaries()

        val coverImage = getCoverImage(titleInfo, binaries)
        // fixme manyImages
        val manyImags = emptyList<ImageModel>()
        val result = ParsedBookModel(

            titleInfo = previewBookMapper.map(titleInfo, coverImage, manyImags),
            elements = elementMapper.map(body, binaries)
                .splitPartsBySentences()
                .normalizeEmptyTextUI()
                .normalizeEmptyLines(),
        )
        return result
    }

    private fun getCoverImage(
        titleInfo: Element?,
        binaries: Map<String, ByteArray>,
    ): ImageModel? {
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

        return ImageModel(binaries[ref])
    }
}