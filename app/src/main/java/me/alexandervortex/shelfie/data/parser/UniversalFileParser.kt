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

            val finalFile = if (extension == "zip") {
                val extracted = extractFirstSupportedFileFromZip(outPutFile, booksDir, id)
                // если успешно извлекли — можно безопасно удалить исходный архив
                if (extracted != null && outPutFile.exists()) {
                    outPutFile.delete()
                    Log.d("^_^_Parser", "Deleted source ZIP after extraction: ${outPutFile.name}")
                }
                extracted?.also { extension = it.extension }
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

    ROLLBACK ME

    // region helpers
    /** Извлекает первый fb2/pdf/epub-файл из zip */
    private fun extractFirstSupportedFileFromZip(zipFile: File, booksDir: File, id: String): File? {
        val allowed = listOf("fb2", "epub", "pdf")
        try {
            ZipInputStream(zipFile.inputStream()).use { zip ->
                var entry = runCatching { zip.nextEntry }.getOrNull()
                while (entry != null) {
                    val name = runCatching { decodeEntryNameSafely(entry!!.name) }.getOrDefault("")
                    val ext = allowed.firstOrNull { name.endsWith(".$it") }
                    if (ext != null) {
                        val extracted = File(booksDir, "$id.$ext")
                        FileOutputStream(extracted).use { output ->
                            zip.copyTo(output)
                        }
                        Log.d("^_^_Parser", "Extracted from ZIP: $name")
                        return extracted
                    }
                    entry = runCatching { zip.nextEntry }.getOrNull()
                }
            }
        } catch (e: IllegalArgumentException) {
            // 💡 fallback: читаем без имён (например, win-1251 архива)
            Log.e("^_^_Parser", "Malformed ZIP, retrying fallback mode", e)
            try {
                ZipInputStream(zipFile.inputStream()).use { zip ->
                    val entry = runCatching { zip.nextEntry }.getOrNull() ?: return null
                    val extracted = File(booksDir, "$id.fb2")
                    FileOutputStream(extracted).use { output ->
                        zip.copyTo(output)
                    }
                    Log.d("^_^_Parser", "Fallback extracted unnamed fb2 file")
                    return extracted
                }
            } catch (e2: Exception) {
                Log.e("^_^_Parser", "Fallback failed: ${e2.message}", e2)
            }
        } catch (e: Exception) {
            Log.e("^_^_Parser", "extractFromZip failed: ${e.message}", e)
        }
        return null
    }

    /**
     * Безопасно декодирует имя zip-файла, чтобы не упасть на MALFORMED[1].
     * Пробует UTF-8 → CP866 → Windows-1251.
     */
    private fun decodeEntryNameSafely(rawName: String?): String {
        if (rawName == null) return ""
        return try {
            // если имя читается без ошибок — используем как есть
            rawName.lowercase()
        } catch (e: IllegalArgumentException) {
            try {
                val bytes = rawName.toByteArray(Charsets.ISO_8859_1)
                String(bytes, charset("CP866")).lowercase()
            } catch (e2: Exception) {
                try {
                    val bytes = rawName.toByteArray(Charsets.ISO_8859_1)
                    String(bytes, charset("windows-1251")).lowercase()
                } catch (e3: Exception) {
                    Log.e("^_^_Parser", "Failed to decode zip entry name", e3)
                    ""
                }
            }
        }
    }
    // endregion
}
