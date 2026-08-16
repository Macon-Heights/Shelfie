package me.alexandervortex.shelfie.data.parser

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.alexandervortex.shelfie.data.parser.epub.EpubParser
import me.alexandervortex.shelfie.data.parser.fb2.Fb2Parser
import me.alexandervortex.shelfie.data.parser.pdf.PdfParser
import me.alexandervortex.shelfie.model.ParsedBookModel
import me.alexandervortex.shelfie.model.PreviewBookModel
import me.alexandervortex.shelfie.ui.model.BookUIModel
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class UniversalFileParser
@Inject constructor(
    private val fb2: Fb2Parser,
    private val epub: EpubParser,
    private val pdf: PdfParser,
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
            // fixme "pdf" -> pdf.parse(stream)
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
            bookParser(id, file, extension)?.copy(
                progressIndex = scrollIndex,
                progressOffset = scrollOffset
            )
        }
    }
}
