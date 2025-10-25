package me.alexandervortex.shelfie.data.db.entiry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.alexandervortex.shelfie.data.db.dao.BOOK_TABLE

@Entity(tableName = BOOK_TABLE)
data class BookEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "localPath")
    val localPath: String,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "author")
    val author: String?,

    @ColumnInfo(name = "year")
    val year: String?,

    @ColumnInfo(name = "scrollIndex")
    val scrollIndex: Int = 0,

    @ColumnInfo(name = "scrollOffset")
    val scrollOffset: Int = 0,

    @ColumnInfo(name = "elements") // новое поле
    val elements: Int = 0,
)