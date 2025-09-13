package me.alexandervortex.shelfie.data.db.entiry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.alexandervortex.shelfie.data.db.dao.BOOK_TABLE

@Entity(tableName = BOOK_TABLE)
data class BookEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "uri") val uri: String,
    @ColumnInfo(name = "name") val name: String,
)