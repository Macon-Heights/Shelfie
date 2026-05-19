package me.alexandervortex.shelfie.data.parser

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.alexandervortex.shelfie.base.ext.safeGetFileExtension
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
) {
    fun previewFromUri(uri: Uri): TitleInfoUIModel? {
        return unzipOrNot(uri) { stream, extension ->
            previewParser(stream, extension)
        }
    }

    fun parseAndCopy(uri: Uri): BookUIModel? {
        return unzipOrNot(uri) { _, extension ->
            val booksDir = File(context.filesDir, "books").apply { mkdirs() }
            val id = System.currentTimeMillis().toString()
            val outPutFile = File(booksDir, "$id.$extension")

            openStream(uri) { input ->
                outPutFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            bookParser(id, outPutFile, extension)
        }
    }

    private fun previewParser(
        stream: InputStream,
        extension: String
    ): TitleInfoUIModel? {
        return when (extension) {
            "fb2" -> fictionBookParser.getPreview(stream)
            "epub" -> null
            else -> null
        }
    }

    private fun bookParser(
        id: String,
        outPutFile: File,
        extension: String
    ): BookUIModel? {
        return when (extension) {
            "fb2" -> fictionBookParser.parse(id, outPutFile, 0, 0)
            "epub" -> null
            else -> null
        }
    }

    // region HELPERS
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
                            return@use contentAction.invoke(zipStream, "fb2")
                            // only extension, not full entry name
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
        // fixme epub/fb2
        return fileName.endsWith(".fb2", ignoreCase = true)
    }

    private fun <T> openStream(
        uri: Uri, block: (InputStream) -> T
    ): T? {
        return context.contentResolver.openInputStream(uri)?.use(block)
    }
    // endregion

    // region NEEDS WORK
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
            fictionBookParser.parse(
                id,
                file,
                scrollOffset,
                scrollIndex
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
    // endregion
}
