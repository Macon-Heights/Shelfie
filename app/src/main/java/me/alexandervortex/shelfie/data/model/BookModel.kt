package me.alexandervortex.shelfie.data.model

import android.net.Uri
import com.kursx.parser.fb2.FictionBook

data class BookModel(
    val id: String, // for db and files
    val uri: Uri,
    val fb2: FictionBook,
    val localPath: String,
    val title: String,
    val author: String?,
)