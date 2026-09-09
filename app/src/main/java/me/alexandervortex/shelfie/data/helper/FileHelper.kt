package me.alexandervortex.shelfie.data.helper

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

    suspend fun getFile(path: String): File? = withContext(Dispatchers.IO) {
        val file = File(path)
        if (file.exists()) file else null
    }

    suspend fun saveBookFile(
        id: String,
        extension: String,
        stream: InputStream,
    ): File = withContext(Dispatchers.IO) {
        val outputFile = File(booksDir, "$id.$extension")
        outputFile.outputStream().use { output ->
            stream.copyTo(output)
        }
        outputFile
    }

    suspend fun deleteFiles(paths: List<String>) = withContext(Dispatchers.IO) {
        paths.forEach { path ->
            runCatching {
                File(path).takeIf(File::exists)?.delete()
            }
        }
    }
}
