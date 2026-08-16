package me.alexandervortex.shelfie.data.parser

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileHelper
@Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val booksDir: File
        get() = File(context.filesDir, "books").apply { if (!exists()) mkdirs() }

    @Deprecated("old one")
    fun saveBook(stream: InputStream, extension: String): Pair<String, File> {
        val id = System.currentTimeMillis().toString()
        val outputFile = File(booksDir, "$id.$extension")
        outputFile.outputStream().use { output ->
            stream.copyTo(output)
        }
        return id to outputFile
    }

    fun getFile(path: String): File? {
        val file = File(path)
        return if (file.exists()) file else null
    }

    fun saveBookFile(
        id: String,
        extension: String,
        stream: InputStream,
    ): File {
        val outputFile = File(booksDir, "$id.$extension")
        outputFile.outputStream().use { output ->
            stream.copyTo(output)
        }
        return outputFile
    }

    suspend fun deleteFiles(paths: List<String>) = withContext(Dispatchers.IO) {
        paths.forEach { path ->
            runCatching {
                File(path).takeIf(File::exists)?.delete()
            }
        }
    }
}
