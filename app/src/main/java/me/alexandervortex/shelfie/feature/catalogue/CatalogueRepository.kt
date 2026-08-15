package me.alexandervortex.shelfie.feature.catalogue

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.alexandervortex.shelfie.base.ext.toModel
import me.alexandervortex.shelfie.data.db.BookDao
import me.alexandervortex.shelfie.data.mapper.BookEntityMapper
import me.alexandervortex.shelfie.data.parser.UniversalFileParser
import me.alexandervortex.shelfie.model.CatalogueItemModel
import javax.inject.Inject

class CatalogueRepository
@Inject constructor(
    private val dao: BookDao,
    private val mapper: BookEntityMapper,
    private val parser: UniversalFileParser,
) {

    fun getPreviews(): Flow<List<CatalogueItemModel>> {
        return dao.getPreviews().map { entities ->
            entities.map { it.toModel() }
        }
    }

    suspend fun addBookToDbAndDisk(uri: Uri) {
        val model = parser.parseAndCopy(uri) ?: throw Exception("Book not supported")
        val entity = mapper.toEntity(model)
        dao.addBook(entity)
    }

    suspend fun removeBooks(
        books: List<CatalogueItemModel>
    ) {
        dao.removeBooks(books.map { it.id })
        parser.removeBooks(books.map { it.localPath })
    }
}