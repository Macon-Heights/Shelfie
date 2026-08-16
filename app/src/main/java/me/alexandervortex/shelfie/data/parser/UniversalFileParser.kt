package me.alexandervortex.shelfie.data.parser

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.alexandervortex.shelfie.data.parser.epub.EpubParser
import me.alexandervortex.shelfie.data.parser.fb2.Fb2Parser
import me.alexandervortex.shelfie.data.parser.pdf.PdfParser
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
    private val supportedBooks = setOf("fb2", "epub", "txt", "pdf")

    fun previewFromUri(uri: Uri): PreviewBookModel? {
        return zipHelper.processUriContent(uri, supportedBooks) { stream, extension ->
            previewParser(stream, extension)
        }
    }

    fun parseAndCopy(uri: Uri): BookUIModel? {
        return zipHelper.processUriContent(uri, supportedBooks) { stream, extension ->
            val (id, file) = fileHelper.saveBook(stream, extension)
            bookParser(id, file, extension)
        }
    }

    private fun previewParser(
        stream: InputStream,
        extension: String
    ): PreviewBookModel? {
        return when (extension.lowercase()) {
            "fb2" -> fb2.getPreview(stream)
            "epub" -> epub.getPreview(stream)
            "pdf" -> pdf.getPreview(stream)
            else -> null
        }
    }

    private fun bookParser(
        id: String,
        outPutFile: File,
        extension: String
    ): BookUIModel? {
        return when (extension.lowercase()) {
            "fb2" -> fb2.parse(id, outPutFile, 0, 0)
            "epub" -> epub.parse(id, outPutFile, 0, 0)
            "pdf" -> pdf.parse(id, outPutFile, 0, 0)
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
