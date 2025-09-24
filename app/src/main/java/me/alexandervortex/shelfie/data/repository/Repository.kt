package me.alexandervortex.shelfie.data.repository

import android.net.Uri
import androidx.core.net.toUri
import com.kursx.parser.fb2.FictionBook
import me.alexandervortex.shelfie.data.datasource.FileSystemDataSource
import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.data.mapper.BookEntityMapper
import javax.inject.Inject

class Repository
@Inject constructor(
    private val dao: BookDao,
    private val mapper: BookEntityMapper,
    private val dataSource: FileSystemDataSource,
) {

    suspend fun addBookByUri(uri: Uri) {
        val fictionBook = dataSource.importFromUri(uri)
        val entity: BookEntity = mapper.toEntity(fictionBook, uri)
        dao.insert(entity)
    }

    suspend fun getBookEntities(): List<BookEntity> {
        return dao.getAll()
    }

    suspend fun getBookEntityById(id: String): FictionBook? {
        return dao.getById(id)?.uri?.toUri()?.let { dataSource.importFromUri(it).fb2 }
    }
}
