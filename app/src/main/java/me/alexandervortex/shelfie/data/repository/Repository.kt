package me.alexandervortex.shelfie.data.repository

import android.net.Uri
import me.alexandervortex.shelfie.data.datasource.UniversalFileParser
import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.data.db.mapper.BookEntityMapper
import me.alexandervortex.shelfie.data.model.BookModel
import javax.inject.Inject

class Repository
@Inject constructor(
    private val dao: BookDao,
    private val mapper: BookEntityMapper,
    private val parser: UniversalFileParser,
) {

    suspend fun importFromUri(uri: Uri) {

        /**
         * uri -> universal parser -> book model
         * эта часть понятна и логична и пусть остается так
         */
        val bookModel = parser.importFromUri(uri)


        val entity = mapper.toEntity(bookModel)
        dao.insert(entity)
    }

    suspend fun getBookEntities(): List<BookEntity> {
        return dao.getAll()
    }

    suspend fun getBookModelById(id: String): BookModel? {
        val entity = dao.getById(id)
        return entity?.let {
            parser.loadFile(entity.id, entity.localPath)
        }
    }
}
