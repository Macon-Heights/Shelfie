package me.alexandervortex.shelfie.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.alexandervortex.shelfie.data.db.entity.BookEntity

const val BOOK_TABLE = "book_table"

@Dao
interface BookDao {

    @Query("SELECT * FROM $BOOK_TABLE")
    fun getPreviews(): Flow<List<BookEntity>>

    @Query("SELECT * FROM $BOOK_TABLE WHERE id = :id LIMIT 1")
    suspend fun getPreviewById(id: String): BookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBook(book: BookEntity)

    @Query("UPDATE $BOOK_TABLE SET scrollIndex = :index, scrollOffset = :offset, elements = :elements WHERE id = :id")
    suspend fun updateProgress(
        id: String,
        index: Int,
        offset: Int,
        elements: Int
    )

    @Query("DELETE FROM $BOOK_TABLE WHERE id IN (:ids)")
    suspend fun removeBooks(ids: List<String>)
}