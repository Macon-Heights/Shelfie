package me.alexandervortex.shelfie.base.ext

import android.content.Context
import android.net.Uri
import com.kursx.parser.fb2.FictionBook
import java.io.File

const val MESSAGE = "Не удалось открыть выбранный файл"

/**
 * Копирует содержимое по Uri во временный файл в cacheDir и возвращает этот файл.
 * ВАЖНО: метод синхронный и блокирующий — вызывай его вне Main Thread (IO).
 */
fun Uri?.toBook(context: Context): FictionBook? {
    return this?.let { uri ->
        // Имя выходного файла жёстко задано — каждый вызов перезапишет "current.fb2".
        // Это удобно для простоты, но плохо для многократных импортов/параллельности.
        val outFile = File(context.cacheDir, "current.fb2")

        // Открываем поток чтения из ContentResolver по переданному Uri.
        // openInputStream может вернуть null (например, нет доступа) → тогда бросаем исключение.
        context.contentResolver.openInputStream(uri)?.use { input ->
            // Открываем поток записи в наш временный файл.
            outFile.outputStream().use { output ->
                // Копируем все байты из входного потока в выходной.
                // copyTo использует буфер по умолчанию (8К), этого обычно достаточно.
                input.copyTo(output)
            }
        } ?: throw IllegalStateException(MESSAGE)

        // Возвращаем ссылку на созданный файл в cacheDir.
        outFile
    }?.let {
        FictionBook(it)
    }
}