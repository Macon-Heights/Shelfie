package me.alexandervortex.shelfie.data.model

import java.io.File
import java.io.InputStream

data class BookFile(
    val file: File,
) {

    val name: String get() = file.name
    val path: String get() = file.absolutePath

    fun openInputStream(): InputStream = file.inputStream()
}