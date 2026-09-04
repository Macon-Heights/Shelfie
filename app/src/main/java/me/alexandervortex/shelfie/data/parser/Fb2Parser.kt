package me.alexandervortex.shelfie.data.parser

import me.alexandervortex.shelfie.base.ext.getBinaries
import me.alexandervortex.shelfie.base.ext.getBody
import me.alexandervortex.shelfie.base.ext.getTitleInfo
import me.alexandervortex.shelfie.data.mapper.ElementMapper
import me.alexandervortex.shelfie.data.mapper.PreviewBookMapper
import me.alexandervortex.shelfie.model.ByteImageModel
import me.alexandervortex.shelfie.model.ParsedBookModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import java.io.InputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Fb2Parser
@Inject constructor(
    private val previewBookMapper: PreviewBookMapper,
    private val elementMapper: ElementMapper,
) {

    suspend fun parse(
        inputStream: InputStream
    ): ParsedBookModel = coroutineScope {
        val doc: Document = withContext(Dispatchers.IO) {
            Jsoup.parse(
                inputStream,
                null,
                "",
                Parser.xmlParser()
            )
        }

        val body = doc.getBody()
        val titleInfo = doc.getTitleInfo()
        val binaries = doc.getBinaries()

        val coverImage = getCoverImage(titleInfo, binaries)

        val titleInfoModelDeferred = async {
            previewBookMapper.map(
                titleInfo = titleInfo,
                coverImage = coverImage,
                gallery = emptyList(),
            )
        }

        val document = elementMapper.map(
            root = body,
            binaries = binaries,
        )

        ParsedBookModel(
            titleInfo = titleInfoModelDeferred.await(),
            document = document,
        )
    }

    private fun getCoverImage(
        titleInfo: Element?,
        binaries: Map<String, ByteArray>,
    ): ByteImageModel? {
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

        return ByteImageModel(binaries[ref])
    }
}