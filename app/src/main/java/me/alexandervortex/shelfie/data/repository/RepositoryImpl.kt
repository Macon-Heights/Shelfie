package me.alexandervortex.shelfie.data.repository

import android.net.Uri
import androidx.core.net.toUri
import com.kursx.parser.fb2.FictionBook
import me.alexandervortex.shelfie.data.datasource.Fb2DataSource
import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.db.entiry.BookUri
import me.alexandervortex.shelfie.data.mapper.BookEntityMapper
import javax.inject.Inject

class RepositoryImpl
@Inject constructor(
    private val dao: BookDao,
    private val mapper: BookEntityMapper,
    private val dataSource: Fb2DataSource,
) {

    private var bookUri: BookUri? = null

    suspend fun addBookByUri(uri: Uri) {
        val fictionBook = dataSource.importFromUri(uri)
        val entity = mapper.fromParsed(fictionBook, uri)
        dao.insert(entity)
    }

    suspend fun getBooksUri(): List<BookUri> {
        return dao.getAll()
    }

    suspend fun getBookById(id: String): FictionBook? {
        return dao.getById(id)?.uri?.toUri()?.let { dataSource.importFromUri(it).fb2 }
    }
}
