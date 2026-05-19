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

    private fun previewParser(
        stream: InputStream,
        extension: String
    ): TitleInfoUIModel? {
        return when (extension) {
            "fb2" -> fictionBookParser.getPreview(inputStream = stream)
            "epub" -> null
            else -> null
        }
    }

    fun <T> unzipOrNot(
        uri: Uri,
        contentAction: (InputStream, String) -> T
    ): T? {
        val extension = uri.safeGetFileExtension(context) ?: return null
        return openStream(uri) { stream ->
            if (extension == ZIP_EXT) {
                ZipInputStream(stream).use { zipStream ->
                    var entry = zipStream.nextEntry
                    while (entry != null) {
                        if (!entry.isDirectory && isSupportedContent(entry.name)) {
                            return@use contentAction.invoke(zipStream, "fb2") // only extension, not full entry name
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
        return fileName.endsWith(".fb2", ignoreCase = true)
    }

    // region xz
    private val supportedExtensions = setOf("fb2")

    suspend fun importFromUri(uri: Uri): BookUIModel? {
        val result = withContext(Dispatchers.IO) {
            val extension = uri.safeGetFileExtension(context)
                ?: return@withContext null
            if (extension !in supportedExtensions) {
                return@withContext null
            }

            val booksDir = File(context.filesDir, "books").apply { mkdirs() }
            val id = System.currentTimeMillis().toString()

            val outPutFile = File(booksDir, "$id.$extension")
            context.contentResolver.openInputStream(uri)?.use { input ->
                outPutFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            } ?: error("Не удалось открыть выбранный файл")

            when (extension) {
                "fb2" -> fictionBookParser.parse(id, outPutFile, 0, 0)
                else -> null
            }
        }
        return result
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

    //region hide
    private fun <T> openStream(
        uri: Uri,
        block: (InputStream) -> T
    ): T? {
        return context.contentResolver.openInputStream(uri)?.use(block)
    }
    // endregion
    // endregion
}
