package me.alexandervortex.shelfie.data.datasource

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.alexandervortex.shelfie.data.mapper.BookModelMapper
import me.alexandervortex.shelfie.data.model.BookModel
import java.io.File
import javax.inject.Inject

class FileSystemDataSource
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val mapper: BookModelMapper,
) {

    suspend fun importFromUri(uri: Uri): BookModel {
        return withContext(Dispatchers.IO) {
            val booksDir = File(context.filesDir, "books").apply { mkdirs() }

            val id = System.currentTimeMillis().toString()
            val out = File(booksDir, "$id.fb2")

            context.contentResolver.openInputStream(uri)?.use { input ->
                out.outputStream().use { output ->
                    input.copyTo(output)
                }
            } ?: error("Не удалось открыть выбранный файл")
            mapper.map(out, id)
        }
    }

    suspend fun loadFile(
        id: String,
        localPath: String,
    ): BookModel {
        return withContext(Dispatchers.IO) {
            val file = File(localPath)
            require(file.exists()) { "Файл книги не найден: $localPath" }
            mapper.map(file, id)
        }
    }
}
