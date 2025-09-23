package me.alexandervortex.shelfie.data.repository

import android.net.Uri
import androidx.core.net.toUri
import com.kursx.parser.fb2.FictionBook
import me.alexandervortex.shelfie.data.datasource.Fb2DataSource
import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.db.entiry.BookUri
import me.alexandervortex.shelfie.data.mapper.BookEntityMapper
import javax.inject.Inject

@Deprecated("UDOLI")
class RepositoryImpl
@Inject constructor(
    private val dao: BookDao,
    private val mapper: BookEntityMapper,
    private val dataSource: Fb2DataSource, // parser
) {

    // добавить ури в базу
    private var bookUri: BookUri? = null

    suspend fun addBookByUri(uri: Uri) {
        val fictionBook = dataSource.importFromUri(uri)
        val entity = mapper.fromParsed(fictionBook, uri)
        dao.insert(entity)
    }

    // region deprecated
//    suspend fun addBook(context: Context, uri: Uri) {
//        val book = uri.toBook(context)
//        mapper.map(book, uri)?.let { item ->
//            dao.insert(item)
//        }
//    }

    suspend fun getBooksUri(): List<BookUri> {
        return dao.getAll()
    }

    fun saveTheBook(item: BookUri) {
        bookUri = item
    }

    suspend fun getTheBook(): FictionBook? {
        return bookUri?.uri?.toUri()?.let { dataSource.importFromUri(it).fb2 }
//        return bookUri?.uri?.toUri().toBook(context)?.toFB2Model()
    }
    // endregion

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
