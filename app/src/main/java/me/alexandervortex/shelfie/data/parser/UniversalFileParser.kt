package me.alexandervortex.shelfie.data.parser

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.alexandervortex.shelfie.base.ext.safeGetFileExtension
import me.alexandervortex.shelfie.data.parser.epub.EpubParser
import me.alexandervortex.shelfie.data.parser.fb2.FictionBookParser
import me.alexandervortex.shelfie.ui.model.BookUIModel
import me.alexandervortex.shelfie.ui.model.TitleInfoUIModel
import java.io.File
import java.io.InputStream
import java.util.zip.ZipInputStream
import javax.inject.Inject

private const val ZIP_EXT = "zip"

class UniversalFileParser
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val fictionBookParser: FictionBookParser,
    private val epub: EpubParser
) {
    fun previewFromUri(uri: Uri): TitleInfoUIModel? {
        return unzipOrNot(uri) { stream, extension ->
            previewParser(stream, extension)
        }
    }

    fun parseAndCopy(uri: Uri): BookUIModel? {
        return unzipOrNot(uri) { stream, extension ->
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
    ): TitleInfoUIModel? {
        return when (extension.lowercase()) {
            "fb2" -> fictionBookParser.getPreview(stream)
            "epub" -> epub.getPreview(stream)
            else -> null
        }
    }

    private fun bookParser(
        id: String,
        outPutFile: File,
        extension: String
    ): BookUIModel? {
        return when (extension.lowercase()) {
            "fb2" -> fictionBookParser.parse(id, outPutFile, 0, 0)
            "epub" -> epub.parse(id, outPutFile, 0, 0)
            else -> null
        }
    }

    private fun <T> unzipOrNot(
        uri: Uri, contentAction: (InputStream, String) -> T
    ): T? {
        val extension = uri.safeGetFileExtension(context) ?: return null
        return openStream(uri) { stream ->
            if (extension == ZIP_EXT) {
                ZipInputStream(stream).use { zipStream ->
                    var entry = zipStream.nextEntry
                    while (entry != null) {
                        if (!entry.isDirectory && isSupportedContent(entry.name)) {
                            val innerExt = entry.name.substringAfterLast('.', "")
                            return@use contentAction.invoke(zipStream, innerExt)
                        }
                        entry = zipStream.nextEntry
                    }
                    null
                }
            } else {
                contentAction.invoke(stream, extension)
            }
        }
    }

    private fun isSupportedContent(fileName: String): Boolean {
        val supportedBooks = setOf("fb2", "epub", "txt")
        return supportedBooks.any { fileName.endsWith(it, ignoreCase = true) }
    }

    private fun <T> openStream(
        uri: Uri, block: (InputStream) -> T
    ): T? {
        return context.contentResolver.openInputStream(uri)?.use(block)
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
