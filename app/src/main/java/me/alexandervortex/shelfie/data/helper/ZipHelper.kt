package me.alexandervortex.shelfie.data.helper

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.alexandervortex.shelfie.base.ext.safeGetFileExtension
import java.io.File
import java.io.InputStream
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import javax.inject.Inject

private const val ZIP_EXT = "zip"

class ZipHelper @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    suspend fun <T> processUriContent(
        uri: Uri,
        supportedExtensions: Set<String>,
        contentAction: suspend (InputStream, String) -> T
    ): T? = withContext(Dispatchers.IO) {
        val extension = uri.safeGetFileExtension(context) ?: return@withContext null
        openStream(uri) { stream ->
            if (extension.lowercase() == ZIP_EXT) {
                ZipInputStream(stream).use { zipStream ->
                    var entry = zipStream.nextEntry
                    while (entry != null) {
                        if (!entry.isDirectory && isSupportedContent(entry.name, supportedExtensions)) {
                            val innerExt = entry.name.substringAfterLast('.', "")
                            return@use contentAction.invoke(zipStream, innerExt)
                        }
                        entry = zipStream.nextEntry
                    }
                    null
                }
            } else {
                contentAction.invoke(stream, extension)
            }
        }
    }

    suspend fun <T> useZipFile(inputStream: InputStream, block: suspend (ZipFile) -> T): T = withContext(Dispatchers.IO) {
        val tempFile = File.createTempFile("temp_book", ".zip", context.cacheDir)
        try {
            tempFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            ZipFile(tempFile).use { zipFile ->
                block(zipFile)
            }
        } finally {
            tempFile.delete()
        }
    }

    private fun isSupportedContent(fileName: String, supportedExtensions: Set<String>): Boolean {
        return supportedExtensions.any { fileName.endsWith(it, ignoreCase = true) }
    }

    private suspend fun <T> openStream(
        uri: Uri, block: suspend (InputStream) -> T
    ): T? {
        return context.contentResolver.openInputStream(uri)?.use { 
            block(it)
        }
    }
}
