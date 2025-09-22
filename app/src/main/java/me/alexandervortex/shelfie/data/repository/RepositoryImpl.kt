package me.alexandervortex.shelfie.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import me.alexandervortex.shelfie.base.ext.toBook
import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.db.entiry.BookUri
import me.alexandervortex.shelfie.data.mapper.BookEntityMapper
import me.alexandervortex.shelfie.data.model.FB2Model
import me.alexandervortex.shelfie.data.model.toFB2Model
import javax.inject.Inject

class RepositoryImpl
@Inject constructor(
    private val dao: BookDao,
    private val mapper: BookEntityMapper,
) {

    private var bookUri: BookUri? = null

    suspend fun addBook(context: Context, uri: Uri) {
        val book = uri.toBook(context)
        mapper.map(book, uri)?.let { item ->
            dao.insert(item)
        }
    }

    suspend fun getBooks(): List<BookUri> {
        return dao.getAll()
    }

    fun saveTheBook(item: BookUri) {
        bookUri = item
    }

    fun getTheBook(context: Context): FB2Model? {
//        val model = fb2.importFromUri()

        return bookUri?.uri?.toUri().toBook(context)?.toFB2Model()
    }

    /*
    // NEW
    suspend fun importFromUri(uri: Uri): Long {
        val parsed = fb2.importFromUri(uri)
        val entity = mapper.fromParsed(parsed,uri)  // собери BookEntity из ParsedFb2
        dao.insert(entity)
        return entity.id
    }

    suspend fun importFromAssets(assetName: String): Long {
        val parsed = fb2.importFromAssets(assetName)
        val entity = mapper.fromParsed(parsed, uri)
        dao.insert(entity)
        return entity.id
    }

    suspend fun getAll(): List<BookEntity> = dao.getAll()
    */
}
