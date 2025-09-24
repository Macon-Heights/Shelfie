package me.alexandervortex.shelfie.data.model

import com.kursx.parser.fb2.FictionBook

// main book model for work
data class BookModel(
    // for db and files
    val id: String,
    val localPath: String,

    // for grid view
    val title: String,
    val author: String?,
    val year: String,

    // entire data
    val fb2: FictionBook,
)