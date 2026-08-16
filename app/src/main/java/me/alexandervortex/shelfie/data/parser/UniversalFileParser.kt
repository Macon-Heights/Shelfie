package me.alexandervortex.shelfie.data.parser

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.alexandervortex.shelfie.data.parser.epub.EpubParser
import me.alexandervortex.shelfie.data.parser.fb2.Fb2Parser
import me.alexandervortex.shelfie.model.ParsedBookModel
import me.alexandervortex.shelfie.ui.model.BookUIModel
import java.io.InputStream
import java.util.zip.ZipFile
import javax.inject.Inject

class UniversalFileParser
@Inject constructor(
    private val fb2: Fb2Parser,
    private val epub: EpubParser,
    private val zipHelper: ZipHelper,
    private val fileHelper: FileHelper,
) {

    fun bookParser(
        stream: InputStream,
        extension: String
    ): ParsedBookModel? {
        return when (extension.lowercase()) {
            "fb2" -> fb2.parse(stream)
            "epub" -> zipHelper.useZipFile(stream) { epub.parse(it) }
            else -> null
        }
    }

    suspend fun getBookModelById(
        id: String,
        localPath: String,
        scrollOffset: Int,
        scrollIndex: Int,
    ): BookUIModel? {
        return withContext(Dispatchers.IO) {
            val file = fileHelper.getFile(localPath) ?: return@withContext null
            val extension = file.extension
            val parsed = when (extension.lowercase()) {
                "fb2" -> fb2.parse(file.inputStream())
                "epub" -> ZipFile(file).use { epub.parse(it) }
                else -> null
            } ?: return@withContext null

            BookUIModel(
                id = id,
                localPath = localPath,
                titleInfo = parsed.titleInfo,
                elements = parsed.elements,
                progressIndex = scrollIndex,
                progressOffset = scrollOffset
            )
        }
    }
}
