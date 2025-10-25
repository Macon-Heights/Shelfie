package me.alexandervortex.shelfie.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.alexandervortex.shelfie.data.db.entiry.BookEntity

const val BOOK_TABLE = "book_table"

@Dao
interface BookDao {

    @Query("SELECT * FROM $BOOK_TABLE WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): BookEntity?

    @Query("SELECT * FROM $BOOK_TABLE")
    suspend fun getBookEntities(): List<BookEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: BookEntity)

    @Delete
    suspend fun delete(user: BookEntity)

    @Query("UPDATE $BOOK_TABLE SET scrollIndex = :index, scrollOffset = :offset, elements = :elements WHERE id = :id")
    suspend fun updateProgress(id: String, index: Int, offset: Int, elements: Int)
}