package me.alexandervortex.shelfie.data.datasource

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.alexandervortex.shelfie.base.getFileExtension
import me.alexandervortex.shelfie.data.mapper.FictionBookParser
import me.alexandervortex.shelfie.data.model.BookFile
import me.alexandervortex.shelfie.data.model.BookUi
import java.io.File
import javax.inject.Inject

class UniversalFileParser
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val fictionBookParser: FictionBookParser,
) {

    // пока только fb2
    private val supportedExtensions = setOf("fb2")

    /**
     * тут мы должны решить
     * какой парсер должен парсить нашу книгу
     * должен ли файл быть обработан вообще (подходит ли он мне)
     */
    suspend fun importFromUri(uri: Uri): BookUi? {

        return withContext(Dispatchers.IO) {
            val extension = uri.getFileExtension()
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

            val bookFile = BookFile(outPutFile)
            when (extension) {
                "fb2" -> fictionBookParser.parse(bookFile, id)
                else -> null
            }
        }
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
    ): BookUi? {
        return withContext(Dispatchers.IO) {
            val file = File(localPath)
            val bookFile = BookFile(file)
            if (!file.exists()) {
                return@withContext null
            }
            fictionBookParser.parse(bookFile, id)
        }
    }
}
