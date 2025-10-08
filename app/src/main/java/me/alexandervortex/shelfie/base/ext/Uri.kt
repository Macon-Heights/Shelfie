package me.alexandervortex.shelfie.base.ext

import android.net.Uri

fun Uri.getFileExtension(): String? {
    val name = lastPathSegment ?: return null
    val extension = name.substringAfterLast('.', "").lowercase()
    return extension.ifBlank { null }
}