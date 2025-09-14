package me.alexandervortex.shelfie.data.db.entiry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.alexandervortex.shelfie.data.db.dao.BOOK_TABLE

@Entity(tableName = BOOK_TABLE)
data class BookEntity(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "fb2DocumentId")
    val fb2DocumentId: String?,

    @ColumnInfo(name = "uri")
    val uri: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "author")
    val author: String?,

    @ColumnInfo(name = "image")
    val image: ByteArray?,
)