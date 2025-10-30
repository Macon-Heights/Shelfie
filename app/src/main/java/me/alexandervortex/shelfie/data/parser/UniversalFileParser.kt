package me.alexandervortex.shelfie.data.parser

import android.content.Context
import android.net.Uri
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.alexandervortex.shelfie.base.ext.safeGetFileExtension
import me.alexandervortex.shelfie.ui.model.BookUI
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream
import javax.inject.Inject

class UniversalFileParser
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val fictionBookParser: FictionBookParser,
) {

    // можно будет расширять
    private val supportedExtensions = setOf("fb2", "zip")

    suspend fun importFromUri(uri: Uri): BookUI? {
        val result = withContext(Dispatchers.IO) {
            var extension = uri.safeGetFileExtension(context)
                ?: return@withContext null

            val booksDir = File(context.filesDir, "books").apply { mkdirs() }
            val id = System.currentTimeMillis().toString()

            // сохраняем исходный файл
            val outPutFile = File(booksDir, "$id.$extension")
            context.contentResolver.openInputStream(uri)?.use { input ->
                outPutFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            } ?: error("Не удалось открыть выбранный файл")

            // если это ZIP — пробуем извлечь первый fb2/pdf/epub
            val finalFile = if (extension == "zip") {
                extractFirstSupportedFileFromZip(outPutFile, booksDir, id)
                    ?.also { extracted ->
                        extension = extracted.extension
                    }
            } else outPutFile

            // если после извлечения всё ещё неизвестный формат — выходим
            if (extension !in supportedExtensions && finalFile == null) {
                Log.e("^_^_Parser", "Unsupported file type: $extension")
                return@withContext null
            }

            // основной парсер
            when (extension) {
                "fb2" -> fictionBookParser.parse(id, finalFile!!, 0, 0)
                // будущие форматы (epub, pdf и т.д.)
                "epub" -> null
                "pdf" -> null
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
    ): BookUI? {
        val result = withContext(Dispatchers.IO) {
            val file = File(localPath)
            if (!file.exists()) return@withContext null
            fictionBookParser.parse(id, file, scrollOffset, scrollIndex)
        }
        return result
    }

    suspend fun removeBooksByPath(paths: List<String>) {
        withContext(Dispatchers.IO) {
            paths.forEach { path ->
                try {
                    val file = File(path)
                    if (file.exists()) file.delete()
                } catch (e: Exception) {
                    Log.e("^_^_Parser", "removeBooksByPath error", e)
                }
            }
        }
    }

    // region helpers
    /** Извлекает первый fb2/pdf/epub-файл из zip */
    private fun extractFirstSupportedFileFromZip(zipFile: File, booksDir: File, id: String): File? {
        val allowed = listOf("fb2", "epub", "pdf")
        return try {
            ZipInputStream(zipFile.inputStream()).use { zip ->
                var entry = zip.nextEntry
                while (entry != null) {
                    val name = entry.name.lowercase()
                    val ext = allowed.firstOrNull { name.endsWith(".$it") }
                    if (ext != null) {
                        val extracted = File(booksDir, "$id.$ext")
                        FileOutputStream(extracted).use { output ->
                            zip.copyTo(output)
                        }
                        Log.d("^_^_Parser", "Extracted from ZIP: ${entry.name}")
                        return extracted
                    }
                    entry = zip.nextEntry
                }
            }
            null
        } catch (e: Exception) {
            Log.e("^_^_Parser", "extractFromZip failed: ${e.message}", e)
            null
        }
    }
    // endregion
}
