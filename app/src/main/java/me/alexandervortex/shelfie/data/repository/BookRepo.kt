package me.alexandervortex.shelfie.data.repository

import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import javax.inject.Inject

class BookRepo
@Inject constructor(
    private val dao: BookDao,
) {

    var currentBook: BookEntity? = null

    suspend fun insert(book: BookEntity) {
        dao.insert(book)
    }

    suspend fun getAll(): List<BookEntity> {
        val result = dao.getAll()
        return result
    }
}