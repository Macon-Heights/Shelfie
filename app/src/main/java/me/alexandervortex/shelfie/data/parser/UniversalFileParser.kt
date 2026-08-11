package me.alexandervortex.shelfie.data.parser

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.alexandervortex.shelfie.data.parser.epub.EpubParser
import me.alexandervortex.shelfie.data.parser.fb2.Fb2Parser
import me.alexandervortex.shelfie.data.parser.pdf.PdfParser
import me.alexandervortex.shelfie.features.screens.preview.PreviewScreenUIModel
import me.alexandervortex.shelfie.ui.model.BookUIModel
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class UniversalFileParser
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val fb2: Fb2Parser,
    private val epub: EpubParser,
    private val pdf: PdfParser,
    private val zipHelper: ZipHelper,
) {
    private val supportedBooks = setOf("fb2", "epub", "txt", "pdf")

    fun previewFromUri(uri: Uri): PreviewScreenUIModel? {
        return zipHelper.processUriContent(uri, supportedBooks) { stream, extension ->
            previewParser(stream, extension)
        }
    }

    fun parseAndCopy(uri: Uri): BookUIModel? {
        return zipHelper.processUriContent(uri, supportedBooks) { stream, extension ->
            val booksDir = File(context.filesDir, "books").apply { mkdirs() }
            val id = System.currentTimeMillis().toString()
            val outputFile = File(booksDir, "$id.$extension")

            outputFile.outputStream().use { output ->
                stream.copyTo(output)
            }
            bookParser(id, outputFile, extension)
        }
    }

    private fun previewParser(
        stream: InputStream,
        extension: String
    ): PreviewScreenUIModel? {
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
        val result = withContext(Dispatchers.IO) {
            val file = File(localPath)
            if (!file.exists()) {
                return@withContext null
            }
            val extension = file.extension
            bookParser(id, file, extension)?.copy(
                progressIndex = scrollIndex,
                progressOffset = scrollOffset
            )
        }
        return result
    }

    suspend fun removeBooks(paths: List<String>) = withContext(Dispatchers.IO) {
        paths.forEach {
            runCatching {
                File(it)
                    .takeIf(File::exists)
                    ?.delete()
            }
        }
    }
}
