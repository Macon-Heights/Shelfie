package me.alexandervortex.shelfie.data.datasource

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.kursx.parser.fb2.FictionBook
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class Fb2DataSource
@Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend fun importFromUri(uri: Uri): Parsed = withContext(Dispatchers.IO) {
        val out = File(context.cacheDir, "book_${System.currentTimeMillis()}.fb2")
        context.contentResolver.openInputStream(uri)?.use { input ->
            out.outputStream().use { output ->
                output.write(input.readBytes())
            }
        } ?: error("Не удалось открыть выбранный файл")
        parse(out, uri)
    }

    suspend fun importFromAssets(assetName: String): Parsed = withContext(Dispatchers.IO) {
        val out = File(context.cacheDir, assetName)
        context.assets.open(assetName).use { input ->
            out.outputStream().use { it.write(input.readBytes()) }
        }
        parse(out, out.toUri())
    }

    private fun parse(file: File, uri: Uri): Parsed {
        val fb = FictionBook(file)
        val title = fb.description?.titleInfo?.bookTitle ?: file.nameWithoutExtension
        val author = fb.description?.titleInfo?.authors?.firstOrNull()?.fullName

        return Parsed(
            localPath = file.absolutePath,
            title = title,
            author = author,
            fb2 = fb,
            uri = uri,
            id = fb.body.toString()
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