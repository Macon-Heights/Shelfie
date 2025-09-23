package me.alexandervortex.shelfie.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.alexandervortex.shelfie.data.db.entiry.BookUri

const val BOOK_TABLE = "book_table"

@Dao
interface BookDao {

    @Query("SELECT * FROM $BOOK_TABLE WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): BookUri?

    @Query("SELECT * FROM $BOOK_TABLE")
    suspend fun getAll(): List<BookUri>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: BookUri)

    @Delete
    suspend fun delete(user: BookUri)
}