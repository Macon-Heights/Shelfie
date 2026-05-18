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
import javax.inject.Inject

class UniversalFileParser
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val fictionBookParser: FictionBookParser,
) {

    private val supportedExtensions = setOf("fb2")

    fun previewFromUri(uri: Uri): TitleInfoUIModel? {
        return context.contentResolver.openInputStream(uri)?.use { stream ->
            fictionBookParser.parseTitleInfo(
                inputStream = stream,
            )
        }
    }

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

    /**
     * тут мы берем файл по айди и пути
     * делаем из него book model
     * и возвращаем
     *
     * проверок не нужно
     * сюда попали только валидные книги
     */
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
}
