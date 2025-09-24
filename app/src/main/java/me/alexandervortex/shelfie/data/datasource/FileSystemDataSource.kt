package me.alexandervortex.shelfie.data.datasource

import android.content.Context
import android.net.Uri
import com.kursx.parser.fb2.FictionBook
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class FileSystemDataSource
@Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend fun importFromUri(uri: Uri): Parsed {
        return withContext(Dispatchers.IO) {
            val booksDir = File(context.filesDir, "books").apply { mkdirs() }

            val id = System.currentTimeMillis().toString()
            val out = File(booksDir, "$id.fb2")

            context.contentResolver.openInputStream(uri)?.use { input ->
                out.outputStream().use { output ->
                    input.copyTo(output)
                }
            } ?: error("Не удалось открыть выбранный файл")
            parse(out, uri, id)
        }
    }

    private fun parse(file: File, uri: Uri, id: String): Parsed {
        val fb = FictionBook(file)
        val title = fb.description?.titleInfo?.bookTitle ?: file.nameWithoutExtension
        val author = fb.description?.titleInfo?.authors?.firstOrNull()?.fullName

        return Parsed(
            id = id,
            uri = uri,
            fb2 = fb,
            localPath = file.absolutePath,
            title = title,
            author = author,
        )
    }
}

data class Parsed(
    val id: String,
    val uri: Uri,
    val fb2: FictionBook,
    val localPath: String,
    val title: String,
    val author: String?,
)