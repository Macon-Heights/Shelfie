package me.alexandervortex.shelfie.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import me.alexandervortex.shelfie.data.db.entiry.BookEntity

const val BOOK_TABLE = "book_table"

@Dao
interface BookDao {

    @Query("SELECT * FROM $BOOK_TABLE")
    fun getAll(): List<BookEntity>

    @Insert
    fun insert(book: BookEntity)

    @Delete
    fun delete(user: BookEntity)
}