package me.alexandervortex.shelfie.data.db.entiry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.alexandervortex.shelfie.data.db.dao.BOOK_TABLE

@Entity(tableName = BOOK_TABLE)
data class BookUri(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "uri")
    val uri: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "author")
    val author: String?,

    @ColumnInfo(name = "localPath")
    val localPath: String,
)