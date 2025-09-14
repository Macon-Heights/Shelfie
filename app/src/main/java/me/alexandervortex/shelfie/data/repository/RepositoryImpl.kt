package me.alexandervortex.shelfie.data.repository

import android.net.Uri
import com.kursx.parser.fb2.FictionBook
import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.data.mapper.BookEntityMapper
import javax.inject.Inject

class RepositoryImpl
@Inject constructor(
    private val dao: BookDao, // todo
    private val mapper: BookEntityMapper,
) {

    var currentBook: BookEntity? = null
    var books = mutableListOf<BookEntity>()

    suspend fun addBook(book: FictionBook?, uri: Uri) {
        val bk = mapper.map(book, uri)
        bk?.let { books.add(bk) }
    }

    suspend fun getBooks(): List<BookEntity> {
        return books
    }
}
