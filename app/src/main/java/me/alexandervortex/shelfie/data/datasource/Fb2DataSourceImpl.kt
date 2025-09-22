package me.alexandervortex.shelfie.data.datasource

import android.content.Context
import android.net.Uri
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

    suspend fun importFromUri(uri: Uri): FictionBook = withContext(Dispatchers.IO) {
        // Имя выходного файла жёстко задано — каждый вызов перезапишет "current.fb2".
        // Это удобно для простоты, но плохо для многократных импортов/параллельности.

        //         val outFile = File(context.cacheDir, "current.fb2")
        val out = File(context.cacheDir, "book_${System.currentTimeMillis()}.fb2")

        // Открываем поток чтения из ContentResolver по переданному Uri.
        // openInputStream может вернуть null (например, нет доступа) → тогда бросаем исключение.
        context.contentResolver.openInputStream(uri)?.use { input ->
            // Открываем поток записи в наш временный файл.
            out.outputStream().use { output ->
                //                input.copyTo(output)
                output.write(input.readBytes())
            }
        } ?: error("Не удалось открыть выбранный файл")
        FictionBook(out)
    }

    suspend fun importFromAssets(assetName: String): FictionBook = withContext(Dispatchers.IO) {
        val out = File(context.cacheDir, assetName)
        context.assets.open(assetName).use { input ->
            out.outputStream().use { it.write(input.readBytes()) }
        }
        FictionBook(out)
    }
}