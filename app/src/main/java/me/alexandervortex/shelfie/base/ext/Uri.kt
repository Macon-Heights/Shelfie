package me.alexandervortex.shelfie.base.ext

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

fun Uri.getFileExtension(): String? {
    val name = lastPathSegment ?: return null
    val extension = name.substringAfterLast('.', "").lowercase()
    return extension.ifBlank { null }
}

fun Uri.safeGetFileExtension(context: Context): String? {
    // 1. пробуем MIME type (например "application/x-fictionbook+xml")
    val mime = context.contentResolver.getType(this)
    if (mime == "application/x-fictionbook+xml" || mime == "text/xml") return "fb2"

    // 2. пробуем имя файла из метаданных (Downloads, SAF)
    return context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (nameIndex != -1 && cursor.moveToFirst()) {
            val name = cursor.getString(nameIndex)
            name.substringAfterLast('.', "")
        } else null
    }
}
