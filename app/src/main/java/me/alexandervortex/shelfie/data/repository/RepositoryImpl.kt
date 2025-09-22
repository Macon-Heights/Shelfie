package me.alexandervortex.shelfie.data.repository

import android.net.Uri
import com.kursx.parser.fb2.FictionBook
import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.data.mapper.BookEntityMapper
import javax.inject.Inject

class RepositoryImpl
@Inject constructor(
    private val dao: BookDao,
    private val mapper: BookEntityMapper,
) {

    private var currentBook: BookEntity? = null

    suspend fun addBook(book: FictionBook?, uri: Uri) {
        mapper.map(book, uri)?.let { item ->
            dao.insert(item)
        }
    }

    suspend fun getBooks(): List<BookEntity> {
        return dao.getAll()
    }

    fun saveBook(item: BookEntity) {
        currentBook = item
    }

    fun loadBook(): BookEntity? {
        return currentBook
    }
}
