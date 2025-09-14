package me.alexandervortex.shelfie.data.db.entiry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.alexandervortex.shelfie.data.db.dao.BOOK_TABLE

@Entity(tableName = BOOK_TABLE)
data class BookEntity(
    @PrimaryKey
    val id: Int,  // contentId (SHA-256 текста/канон. XML)

    @ColumnInfo(name = "fb2DocumentId")
    val fb2DocumentId: String?, // <document-info><id>

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "author")
    val author: String,

    @ColumnInfo(name = "lang")
    val lang: String,

    @ColumnInfo(name = "version")
    val version: Int,

    @ColumnInfo(name = "uri")
    val uri: String,

    @ColumnInfo(name = "addedAt")
    val addedAt: Long,
)